package be.gallifreyan.eclipse.archetype.plugin.ui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;

import be.gallifreyan.eclipse.archetype.plugin.mvn.creation.CreateProject;
import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Archetype;
import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Project;

public class ArchetypeWizard extends Wizard {
	ArchetypePage personalInfoPage;
	private CreateProject createProject;

	public void addPages() {
		this.setWindowTitle("Custom Archetype Wizard");
		personalInfoPage = new ArchetypePage("Personal Information Page");
		addPage(personalInfoPage);
	}

	public boolean performFinish() {
		buildProject();
		try {
			ProgressMonitorDialog p = new ProgressMonitorDialog(this.getShell());
			p.run(true, true, createProject);
		
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		showCompletionDialog();
		return true;
	}

	private void buildProject() {
		Project project = new Project();
		Archetype ma = new Archetype("org.apache.maven.archetypes",
				"maven-archetype-quickstart", "1.1");
		project.setName("testnam");
		project.setMavenArchetype(ma);
		createProject = new CreateProject();
		createProject.createProjectWithMavenCore(project, "c:/");
	}
	
	private void showCompletionDialog() {
		MessageDialog.openInformation(this.getShell(), "Custom Archetype Wizard", "Project creation was succesfull!");
	}
}
