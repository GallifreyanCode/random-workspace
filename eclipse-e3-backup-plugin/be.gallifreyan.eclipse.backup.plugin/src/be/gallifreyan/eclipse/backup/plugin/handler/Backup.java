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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class Backup extends AbstractHandler {
	private QualifiedName path = new QualifiedName("d", "path");
	private ExecutionEvent event;
	private IStructuredSelection selection;
	private PromptPanel promptPanel;

	/**
	 * Main execution when menuitem is used.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		selection = (IStructuredSelection) HandlerUtil
				.getActiveMenuSelection(event);
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		this.event = event;

		promptPanel = new PromptPanel(window.getShell());
		promptPanel.show(this);
		return null;
	}

	/**
	 * Starts the packaging process when a destination is chosen in the UI. It
	 * restricts the packaging to a number of formats.
	 * 
	 * @param destination
	 */
	public void createPackage(Destination destination) {
		Object firstElement = selection.getFirstElement();
		if (firstElement instanceof IOpenable) {
			/* Project folder */
			if (firstElement instanceof IJavaProject) {
				IJavaProject object = (IJavaProject) firstElement;
				IResource resource = object.getResource();
				branchToDestination(resource, destination);
			}
			/* Source folder */
			if (firstElement instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot object = (IPackageFragmentRoot) firstElement;
				IResource resource = object.getResource();
				branchToDestination(resource, destination);
			}
			/* Package folder */
			if (firstElement instanceof IPackageFragment) {
				IPackageFragment object = (IPackageFragment) firstElement;
				IResource resource = object.getResource();
				branchToDestination(resource, destination);
			}
			/* Class file */
			if (firstElement instanceof ICompilationUnit) {
				ICompilationUnit object = (ICompilationUnit) firstElement;
				IResource resource = object.getResource();
				branchToDestination(resource, destination);
			}
		} else {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event),
					"Information", "Filetype can not be used.");
		}
	}

	/**
	 * Branches out the process to the specific destinations.
	 * 
	 * @param resource
	 * @param destination
	 */
	private void branchToDestination(IResource resource, Destination destination) {
		switch (destination) {
		case HDD:
			createOnHDD(resource);
			break;
		case USB:
			createOnHDD(resource);
			break;
		case DROPBOX:
			createOnDropbox(resource);
			break;
		case GDRIVE:
			createOnGDrive(resource);
			break;
		}
		promptPanel.close();
	}

	/**
	 * Finds the Dropbox path and passes its location to createOnCloud.
	 * 
	 * @param resource
	 */
	private void createOnDropbox(IResource resource) {
		File root = new File(System.getProperty("user.home") + "\\Dropbox");
		if (root.exists()) {
			createOnCloud(resource, root);
		} else {
			showErrorDialog("No Dropbox found.");
		}
	}

	/**
	 * Finds the Dropbox path and passes its location to createOnCloud.
	 * 
	 * @param resource
	 */
	private void createOnGDrive(IResource resource) {
		File root = new File(System.getProperty("user.home") + "\\Google Drive");
		if (root.exists()) {
			createOnCloud(resource, root);
		} else {
			showErrorDialog("No Google Drive found.");
		}
		createOnCloud(resource, root);
	}

	/**
	 * Creates or uses the existing cloud backup folder, and saves the archive
	 * in it.
	 * 
	 * @param resource
	 * @param root
	 */
	private void createOnCloud(IResource resource, File root) {
		String directory = checkForDestinationFolder(root);
		triggerZipCreation(directory, resource);
	}

	private String checkForDestinationFolder(File root) {
		File destinationFolder = new File(root + "\\Code Backup");
		String directory = root + "\\Code Backup";

		if (!destinationFolder.exists()) {
			new File(root + "\\Code Backup").mkdir();
		}
		return directory;
	}

	/**
	 * Shows a filechooser or uses last used directory to store the zip archive.
	 * 
	 * @param resource
	 */
	private void createOnHDD(IResource resource) {
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
			triggerZipCreation(directory, resource);
		}
	}

	private void triggerZipCreation(String directory, IResource resource) {
		setPersistentProperty(resource, path, directory);
		createZip(directory, resource.getName(), resource.getLocationURI());
	}

	/**
	 * Creates the actual zip.
	 * 
	 * @param directory
	 * @param fileName
	 * @param location
	 */
	private void createZip(String directory, String fileName, URI location) {
		String path = "";
		try {
			path = location.toURL().toString();
			path = path.substring(6);

			String destination = directory + "\\" + fileName + ".zip";
			ZipOutputStream zipOutputStream = new ZipOutputStream(
					new FileOutputStream(destination));

			File folder = new File(path);
			int pathSize = folder.getAbsolutePath().lastIndexOf(File.separator);
			String name = folder.getAbsolutePath().substring(0, pathSize + 1);

			addFolderToZip(folder, name, zipOutputStream);

			zipOutputStream.close();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds folders and its files to the zip that is being created.
	 * 
	 * @param folder
	 * @param name
	 * @param zipOutputStream
	 * @throws IOException
	 */
	private static void addFolderToZip(File folder, String name,
			ZipOutputStream zipOutputStream) throws IOException {
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

	/**
	 * Method for creating error messages.
	 * 
	 * @param reason
	 */
	private void showErrorDialog(String reason) {
		Status status = new Status(IStatus.ERROR, "be.gallifreyan.eclipse.backup.plugin", 0,
				reason, null);
		ErrorDialog.openError(promptPanel.getShell(), "Backup Plugin",
				"Backup process failed", status);
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
