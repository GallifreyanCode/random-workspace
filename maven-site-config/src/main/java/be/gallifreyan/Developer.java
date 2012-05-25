package be.gallifreyan;

public class Developer extends Employee {
	private String skill;
	
	public Developer(String firstName, String lastName, String skill) {
		super(lastName, firstName);
		this.skill = skill;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}
}
