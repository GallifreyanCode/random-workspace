package be.gallifreyan.javaee.entity;


import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import be.gallifreyan.javaee.service.ejb.UserService;

@Entity
@Table(name = "USERS")
@NamedQuery(name = "User.findAllUsers", query = "SELECT u FROM User u")
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	// {{ userId
	@Id
	@Column(nullable = false, length = 50)
	@NotNull(message = "{User.userId.notNull}")
	@Size(min = 1, max = 50, message = "{User.userId.size}")
	private String userId;

	/**
	 * Returns the user Id of the User.
	 * 
	 * @return The user Id.
	 */
	public String getUserId()
	{
		return this.userId;
	}

	/**
	 * Sets the user Id of the User. This should not be invoked for persistent
	 * {@link User} instances.
	 * 
	 * @param userId
	 *            The user Id
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	// }}

	// {{ password
	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false, length = 128)
	@NotNull(message = "{User.password.notNull}")
	@Size(min = 1, max = 500, message = "{User.password.size}")
	private char[] password;

	/**
	 * Returns the password of the user.
	 * 
	 * @return The user's password
	 */
	public char[] getPassword()
	{
		return this.password;
	}

	/**
	 * Sets the password of the user. This can be used to set the user's
	 * password in plain text. It will later be reset to a password digest by
	 * {@link UserService#modifyPassword(be.gallifreyan.javaee.service.ejb.ModifyPasswordRequest)}
	 * .
	 * 
	 * @param password
	 *            The user's password.
	 */
	public void setPassword(char[] password)
	{
		this.password = password;
	}

	// }}

	// {{ albums
	// bi-directional many-to-one association to Album
	@OneToMany(mappedBy = "user", cascade = { CascadeType.ALL }, orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Album> albums;

	/**
	 * Returns the set of albums owned by the user.
	 * 
	 * @return A {@link Set} of albums.
	 */
	public Set<Album> getAlbums()
	{
		return this.albums;
	}

	/**
	 * Sets the albums owned by the user.
	 * 
	 * This method should not be invoked by a caller. Use
	 * {@link User#addToAlbums(Album)} and {@link User#removeFromAlbums(Album)}
	 * to modify the album collection.
	 * 
	 * @param albums
	 *            The albums owned by the user.
	 */
	public void setAlbums(Set<Album> albums)
	{
		this.albums = albums;
	}

	/**
	 * Adds an album to the set of albums owned by the user. Duplicates and null
	 * instances are ignored by this method. The method is used to implement the
	 * mutual registration pattern between the {@link User} and {@link Album}
	 * classes.
	 * 
	 * @param album
	 *            The album to add to the set.
	 */
	public void addToAlbums(final Album album)
	{
		// check for no-op
		if (album == null || getAlbums().contains(album))
		{
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

	/**
	 * Removes an album from the set of albums owned by the user. Non-existent
	 * instances and null instances are ignored by this method. The method is
	 * used to implement the mutual registration pattern between the
	 * {@link User} and {@link Album} classes.
	 * 
	 * @param album
	 *            The album to be removed from the set.
	 */
	public void removeFromAlbums(final Album album)
	{
		// check for no-op
		if (album == null || !getAlbums().contains(album))
		{
			return;
		}
		// dissociate arg
		getAlbums().remove(album);
		album.setUser(null);
		// additional business logic
		onRemoveFromAlbums(album);
	}

	protected void onAddToAlbums(final Album album)
	{
	}

	protected void onRemoveFromAlbums(final Album album)
	{
	}

	// }}

	// {{ groups
	// bi-directional many-to-many association to Group
	@ManyToMany(cascade = { CascadeType.REFRESH, CascadeType.DETACH }, fetch = FetchType.LAZY)
	@JoinTable(name = "USERS_GROUPS", joinColumns = { @JoinColumn(name = "USERID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "GROUPID", nullable = false) })
	private Set<Group> groups;

	/**
	 * Returns the set of groups that the user belongs to.
	 * 
	 * @return A set of groups.
	 */
	public Set<Group> getGroups()
	{
		return this.groups;
	}

	/**
	 * Sets the set of groups that the user belongs to. This method should not
	 * be invoked by a caller. Use the {@link User#addToGroups(Group)} and
	 * {@link User#removeFromGroups(Group)} methods to modify the set of groups
	 * that the user belongs to.
	 * 
	 * @param groups
	 *            A set of groups.
	 */
	public void setGroups(Set<Group> groups)
	{
		this.groups = groups;
	}

	/**
	 * Add the group to the set of groups that the user belongs to. Duplicates
	 * and null instances are ignored by this method. The method is used to
	 * implement the mutual registration pattern between the {@link User} and
	 * {@link Group} classes.
	 * 
	 * @param group
	 *            The group that the user should belong to
	 */
	public void addToGroups(final Group group)
	{
		// check for no-op
		if (group == null || getGroups().contains(group))
		{
			return;
		}
		// associate arg
		group.getUsers().add(this);
		getGroups().add(group);
		// additional business logic
		onAddToGroups(group);
	}

	/**
	 * Removes the group to the set of groups that the user belongs to.
	 * Duplicates and null instances are ignored by this method. The method is
	 * used to implement the mutual registration pattern between the
	 * {@link User} and {@link Group} classes.
	 * 
	 * @param group
	 *            The group that the user should no longer belong to
	 */
	public void removeFromGroups(final Group group)
	{
		// check for no-op
		if (group == null || !getGroups().contains(group))
		{
			return;
		}
		// dissociate arg
		group.getUsers().remove(this);
		getGroups().remove(group);
		// additional business logic
		onRemoveFromGroups(group);
	}

	protected void onAddToGroups(final Group group)
	{
	}

	protected void onRemoveFromGroups(final Group group)
	{
	}

	// }}

	/**
	 * The default constructor. This is meant to be invoked directly only in
	 * unit tests.
	 */
	protected User()
	{
		this.albums = new HashSet<Album>();
		this.groups = new HashSet<Group>();
	}

	/**
	 * @param userId
	 *            The user Id of the user.
	 * @param password
	 *            The password of the user.
	 */
	public User(String userId, char[] password)
	{
		this();
		this.userId = userId;
		this.password = password;
	}

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return (userId == null ? other.userId == null : userId.equals(other.userId));
	}

	@Override
	public String toString()
	{
		return "User [userId=" + userId + "]";
	}

}