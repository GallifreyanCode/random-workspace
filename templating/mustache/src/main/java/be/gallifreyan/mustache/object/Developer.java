package be.gallifreyan.mustache.object;

import java.util.List;

public class Developer {
	protected String firstName, lastName;
	private String role;
	protected List<Project> projects;
	
	public Developer(String firstName, String lastName, String role, List<Project> projects) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.projects = projects;
	}
	
	public String getRole(){
		return role;
	}
	public String getCompany(){
		return "Company";
	}
}
