package be.gallifreyan.eclipse.archetype.plugin.mvn.model;

import java.io.Serializable;

public class Project implements Serializable {
	private static final long serialVersionUID = 3552005546366448471L;
	private String name;
	private String description;
	private String scmUrl;
	private String sourceEncoding = "UTF-8";
	private boolean signArtifactReleases = false;

	private Archetype mavenArchetype;
	private String scmMavenPrefix;

	public Archetype getMavenArchetype() {
		return mavenArchetype;
	}

	public void setMavenArchetype(final Archetype mavenArchetype) {
		this.mavenArchetype = mavenArchetype;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setScmUrl(final String scmUrl) {
		this.scmUrl = scmUrl;
	}

	public String getScmUrl() {
		return scmUrl;
	}

	public String getScmMavenUrl() {
		return getScmMavenPrefix() + getScmUrl();
	}

	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}

	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSignArtifactReleases(boolean signArtifactReleases) {
		this.signArtifactReleases = signArtifactReleases;
	}

	public boolean isSignArtifactReleases() {
		return signArtifactReleases;
	}

	public void setScmMavenPrefix(final String scmMavenPrefix) {
		this.scmMavenPrefix = scmMavenPrefix;
	}

	public String getScmMavenPrefix() {
		return scmMavenPrefix;
	}
}
