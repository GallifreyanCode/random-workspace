package be.gallifreyan.e3.plugin.mvn.creation;

import java.io.File;

import be.gallifreyan.e3.plugin.mvn.model.Project;

public class CreateProjectUtil {

	public void createProject(final Project project) {
			createProjectMethod(project);
	}

	private void createProjectMethod(final Project project) {
		File pom = new File("c:/", project.getName() + "/pom.xml");
	}
}