package be.gallifreyan.eclipse.backup.plugin.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class Backup extends AbstractHandler {
	private QualifiedName path = new QualifiedName("d", "path");
	private ExecutionEvent event;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil
				.getActiveMenuSelection(event);
		this.event = event;
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IOpenable) {
			/* Project folder */
			if (firstElement instanceof IJavaProject) {
				IJavaProject object = (IJavaProject) firstElement;
				IResource resource = object.getResource();
				promptDestination(resource);
			}
			/* Source folder */
			if (firstElement instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot object = (IPackageFragmentRoot) firstElement;
				IResource resource = object.getResource();
				promptDestination(resource);
			}
			/* Package folder */
			if (firstElement instanceof IPackageFragment) {
				IPackageFragment object = (IPackageFragment) firstElement;
				IResource resource = object.getResource();
				promptDestination(resource);
			}
			/* Class file */
			if (firstElement instanceof ICompilationUnit) {
				ICompilationUnit object = (ICompilationUnit) firstElement;
				IResource resource = object.getResource();
				promptDestination(resource);
			}
		} else {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
					"Information", "Filetype can not be used.");
		}
		return null;
	}
	
	private void promptDestination(IResource resource) {
		
		/* Prompt for local directory */
		getDirectory(resource);
	}

	private void getDirectory(IResource resource) {
		String directory = "";
		boolean newDirectory = true;
		directory = getPersistentProperty(resource, path);

		if (directory != null && directory.length() > 0) {
			newDirectory = !(MessageDialog.openQuestion(
					HandlerUtil.getActiveShell(event), "Question",
					"Use the previous output directory?"));
		}
		if (newDirectory) {
			DirectoryDialog fileDialog = new DirectoryDialog(
					HandlerUtil.getActiveShell(event));
			directory = fileDialog.open();
		}
		if (directory != null && directory.length() > 0) {
			setPersistentProperty(resource, path, directory);
			createZip(directory, resource.getName(), resource.getLocationURI());
		}

	}
	private void createZip(String directory, String fileName, URI location) {
		String path = "";
		try {
			path = location.toURL().toString();
			path = path.substring(6);
			
			String destination = directory + "\\" + fileName + ".zip";
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(destination));

			File folder = new File(path);
			int pathSize = folder.getAbsolutePath().lastIndexOf(File.separator);
			String name = folder.getAbsolutePath().substring(0, pathSize+1);
			
			addFolderToZip(folder, name, zipOutputStream);
			
			zipOutputStream.close();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addFolderToZip(File folder, String name, ZipOutputStream zipOutputStream) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, name, zipOutputStream);
			} else {
				String entry = file.getAbsolutePath().substring(name.length());
				ZipEntry zipEntry = new ZipEntry(entry);
				zipOutputStream.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zipOutputStream);
				zipOutputStream.closeEntry();
			}
		}
	}

	protected String getPersistentProperty(IResource resource, QualifiedName qn) {
		try {
			return resource.getPersistentProperty(qn);
		} catch (CoreException e) {
			return "";
		}
	}

	protected void setPersistentProperty(IResource res, QualifiedName qn,
			String value) {
		try {
			res.setPersistentProperty(qn, value);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
