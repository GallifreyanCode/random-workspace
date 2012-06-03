package be.gallifreyan.persistence.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

import be.gallifreyan.persistence.abs.AbstractEntity;

@NamedQuery(name = "Developer.findByName", query = "select d from Developer d where d.name = :name")
@Entity
public class Developer extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = -7008506425249644639L;

	public Developer() {
		super();
	}
	
	@ManyToMany
    private final Set<Role> roles = new HashSet<Role>();

    public boolean addRole(Role role) {
        return roles.add(role);
    }

    public Set<Role> getRoles() {
        return roles;
    }
}