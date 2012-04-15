package be.gallifreyan.javaee.entity;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "GROUPS")
@NamedQuery(name = "Group.findAllGroups", query = "SELECT g FROM Group g")
public class Group implements Serializable
{
	private static final long serialVersionUID = 1L;

	// {{ groupId
	@Id
	@Column(nullable = false, length = 20)
	private String groupId;

	/**
	 * Returns the group Id of the Group.
	 * 
	 * @return The group Id
	 */
	public String getGroupId()
	{
		return this.groupId;
	}

	/**
	 * Set the group Id of the Group. This should not be invoked for persistent
	 * {@link Group} instances.
	 * 
	 * @param groupId
	 *            The group Id
	 */
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	// }}

	// {{ users
	// bi-directional many-to-many association to User
	@ManyToMany(mappedBy = "groups", cascade = { CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.REMOVE })
	private Set<User> users;

	/**
	 * Returns the set of users that belong to the group.
	 * 
	 * @return The set of users that belong to the group.
	 */
	public Set<User> getUsers()
	{
		return this.users;
	}

	/**
	 * Sets the set of users that belong to the group. This method should not be
	 * invoked by a caller. Use the {@link Group#addToUsers(User)} and
	 * {@link Group#removeFromUsers(User)} methods to modify the set of users
	 * belonging to the group.
	 * 
	 * @param users
	 *            The set of users that belong to the group.
	 */
	public void setUsers(Set<User> users)
	{
		this.users = users;
	}

	/**
	 * Adds the user to the group. Duplicates and null instances are ignored by
	 * this method. The method is used to implement the mutual registration
	 * pattern between the {@link User} and {@link Group} classes.
	 * 
	 * @param user
	 *            The user to be added to group.
	 */
	public void addToUsers(final User user)
	{
		// check for no-op
		if (user == null || getUsers().contains(user))
		{
			return;
		}
		// delegate to parent to associate
		user.addToGroups(this);
		// additional business logic
		onAddToUsers(user);
	}

	/**
	 * Removes the user from the group. Duplicates and null instances are
	 * ignored by this method. The method is used to implement the mutual
	 * registration pattern between the {@link User} and {@link Group} classes.
	 * 
	 * @param user
	 *            The user to be removed from the group.
	 */
	public void removeFromUsers(final User user)
	{
		// check for no-op
		if (user == null || !getUsers().contains(user))
		{
			return;
		}
		// delegate to parent to dissociate
		user.removeFromGroups(this);
		// additional business logic
		onRemoveFromUsers(user);
	}

	protected void onAddToUsers(final User user)
	{
	}

	protected void onRemoveFromUsers(final User user)
	{
	}

	// }}

	/**
	 * The default constructor. This is meant to be invoked directly only in
	 * unit tests.
	 */
	protected Group()
	{
		this.users = new HashSet<User>();
	}

	/**
	 * @param groupId
	 *            The group Id of the Group
	 */
	public Group(String groupId)
	{
		this();
		this.groupId = groupId;
	}

	@Override
	public final int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (!(obj instanceof Group))
			return false;
		Group other = (Group) obj;
		return (groupId == null ? other.groupId == null : groupId.equals(other.groupId));
	}

	@Override
	public String toString()
	{
		return "Group [groupId=" + groupId + "]";
	}

}