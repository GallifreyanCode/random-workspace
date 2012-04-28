package be.gallifreyan.e3.plugin.mvn.model;

import java.io.Serializable;

public class MavenArchetype implements Serializable {
	private static final long serialVersionUID = -1709105556029048697L;
	private String groupId;
	private String artifactId;
	private String version;

	public MavenArchetype() {
	}

	public MavenArchetype(final String groupId, final String artifactId,
			final String version) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
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

	public String toString() {
		return groupId + ":" + artifactId + ":" + version;
	}
}
