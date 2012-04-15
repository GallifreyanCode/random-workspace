package be.gallifreyan.javaee.entity;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "GROUPS")
@NamedQuery(name = "Group.findAllGroups", query = "SELECT g FROM Group g")
public class Group implements Serializable {
	private static final long serialVersionUID = -8017610171831623249L;

	@Id
	@Column(nullable = false, length = 20)
	private String groupId;

	@ManyToMany(mappedBy = "groups", cascade = { CascadeType.REFRESH,
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REMOVE })
	private Set<User> users;

	public String getGroupId() {
		return this.groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public void addToUsers(final User user) {
		// check for no-op
		if (user == null || getUsers().contains(user)) {
			return;
		}
		// delegate to parent to associate
		user.addToGroups(this);
		// additional business logic
		onAddToUsers(user);
	}

	public void removeFromUsers(final User user) {
		// check for no-op
		if (user == null || !getUsers().contains(user)) {
			return;
		}
		// delegate to parent to dissociate
		user.removeFromGroups(this);
		// additional business logic
		onRemoveFromUsers(user);
	}

	protected void onAddToUsers(final User user) {
	}

	protected void onRemoveFromUsers(final User user) {
	}

	protected Group() {
		this.users = new HashSet<User>();
	}

	public Group(String groupId) {
		this();
		this.groupId = groupId;
	}

	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
		return result;
	}

	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Group))
			return false;
		Group other = (Group) obj;
		return (groupId == null ? other.groupId == null : groupId
				.equals(other.groupId));
	}

	public String toString() {
		return "Group [groupId=" + groupId + "]";
	}
}
