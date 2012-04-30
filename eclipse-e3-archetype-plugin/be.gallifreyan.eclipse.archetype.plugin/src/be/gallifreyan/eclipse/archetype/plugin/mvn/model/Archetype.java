package be.gallifreyan.eclipse.archetype.plugin.mvn.model;

import java.io.Serializable;

public class Archetype implements Serializable {
	private static final long serialVersionUID = -1709105556029048697L;
	private String groupId;
	private String artifactId;
	private String version;
	private String repository;
	private String description;

	public Archetype() {
	}

	public Archetype(final String groupId, final String artifactId,
			final String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}
	
	public Archetype(final String groupId, final String artifactId,
			final String version, final String repository, final String description) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.repository = repository;
		this.description = description;
	}

	public String getArchetypeGroupId() {
		return groupId;
	}

	public String getArchetypeArtifactId() {
		return artifactId;
	}

	public String getArchetypeVersion() {
		return version;
	}

	public String getRepository() {
		return repository;
	}

	public String getDescription() {
		return description;
	}

	public String toString() {
		return groupId + ":" + artifactId + ":" + version;
	}
	
}
