package be.gallifreyan.persistence.entity;

import java.io.Serializable;

import javax.persistence.*;

import be.gallifreyan.persistence.abs.AbstractEntity;

@NamedQuery(name = User.FIND_BY_ACCOUNT, query = "select u from User u where u.accountname = :accountname")
@Entity
public class User extends AbstractEntity implements Serializable {
	private static final long serialVersionUID = 7960777687149777765L;
	public static final String FIND_BY_ACCOUNT = "User.findByAccountname";
	private String accountname;
	private String password;
	private String role = "ROLE_USER";
	
	public User() {
		super();
	}
	
	public User(String accountname, String password, String role) {
		super(accountname);
		this.accountname = accountname;
		this.password = password;
		this.role = role;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
