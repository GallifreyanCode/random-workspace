package be.gallifreyan.eclipse.archetype.plugin.mvn.creation;

import java.io.File;

import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Project;

public class CreateProjectUtil {

	public void createProject(final Project project) {
			createProjectMethod(project);
	}

	private void createProjectMethod(final Project project) {
		File pom = new File("c:/", project.getName() + "/pom.xml");
	}
}