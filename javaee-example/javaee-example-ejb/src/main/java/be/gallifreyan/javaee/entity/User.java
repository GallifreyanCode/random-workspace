package be.gallifreyan.javaee.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USERS")
@NamedQuery(name = "User.findAllUsers", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = -2550140427340680115L;

	@Id
	@Column(nullable = false, length = 50)
	@NotNull(message = "{User.userId.notNull}")
	@Size(min = 1, max = 50, message = "{User.userId.size}")
	private String userId;

	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false, length = 128)
	@NotNull(message = "{User.password.notNull}")
	@Size(min = 1, max = 500, message = "{User.password.size}")
	private char[] password;

	@OneToMany(mappedBy = "user", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Album> albums;

	@ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(name = "USERS_GROUPS", joinColumns = { @JoinColumn(name = "USERID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "GROUPID", nullable = false) })
	private Set<Group> groups;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public char[] getPassword() {
		return this.password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public Set<Album> getAlbums() {
		return this.albums;
	}

	public void setAlbums(Set<Album> albums) {
		this.albums = albums;
	}

	public void addToAlbums(final Album album) {
		// check for no-op
		if (album == null || getAlbums().contains(album)) {
			return;
		}
		// dissociate arg from current parent (if any)
		album.clearUser();
		// associate arg
		album.setUser(this);
		getAlbums().add(album);
		// additional business logic
		onAddToAlbums(album);
	}

	public void removeFromAlbums(final Album album) {
		// check for no-op
		if (album == null || !getAlbums().contains(album)) {
			return;
		}
		// dissociate arg
		getAlbums().remove(album);
		album.setUser(null);
		// additional business logic
		onRemoveFromAlbums(album);
	}

	protected void onAddToAlbums(final Album album) {
	}

	protected void onRemoveFromAlbums(final Album album) {
	}

	public Set<Group> getGroups() {
		return this.groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public void addToGroups(final Group group) {
		// check for no-op
		if (group == null || getGroups().contains(group)) {
			return;
		}
		// associate arg
		group.getUsers().add(this);
		getGroups().add(group);
		// additional business logic
		onAddToGroups(group);
	}

	public void removeFromGroups(final Group group) {
		// check for no-op
		if (group == null || !getGroups().contains(group)) {
			return;
		}
		// dissociate arg
		group.getUsers().remove(this);
		getGroups().remove(group);
		// additional business logic
		onRemoveFromGroups(group);
	}

	protected void onAddToGroups(final Group group) {
	}

	protected void onRemoveFromGroups(final Group group) {
	}

	protected User() {
		this.albums = new HashSet<Album>();
		this.groups = new HashSet<Group>();
	}

	public User(String userId, char[] password) {
		this();
		this.userId = userId;
		this.password = password;
	}

	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return (userId == null ? other.userId == null : userId
				.equals(other.userId));
	}

	public String toString() {
		return "User [userId=" + userId + "]";
	}
}
