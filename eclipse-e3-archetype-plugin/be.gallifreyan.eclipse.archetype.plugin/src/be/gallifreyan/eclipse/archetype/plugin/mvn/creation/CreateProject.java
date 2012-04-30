package be.gallifreyan.eclipse.archetype.plugin.mvn.creation;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.maven.cli.MavenCli;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Archetype;
import be.gallifreyan.eclipse.archetype.plugin.mvn.model.Project;

public class CreateProject implements IRunnableWithProgress {
	private String baseDir;
	private String[] args;
	private String finalName;

	public void createProjectWithMavenCore(final Project project,
			final String baseDir) {
		this.baseDir = baseDir;
		Archetype ma = project.getMavenArchetype();
		args = new String[] { "archetype:generate",
				"-DarchetypeGroupId=" + ma.getArchetypeGroupId(),
				"-DarchetypeArtifactId=" + ma.getArchetypeArtifactId(),
				"-DarchetypeVersion=" + ma.getArchetypeVersion(),
				"-DgroupId=be.brail", "-Dversion=1.0.0-SNAPSHOT",
				"-Dpackage=be.brail", "-DartifactId=" + project.getName(),
				"-DinteractiveMode=false" };
		finalName = baseDir + File.separatorChar + project.getName();
	}

	public void run(IProgressMonitor arg0) throws InvocationTargetException,
			InterruptedException {
		startCreation();
	}
	
	private void startCreation() {
		MavenCli cli = new MavenCli();
		cli.doMain(args, baseDir, System.out, System.err);
	}

	public String getBaseDir() {
		return baseDir;
	}

	public String[] getArgs() {
		return args;
	}

	public void setFinalName(String finalName) {
		this.finalName = finalName;
	}
}
