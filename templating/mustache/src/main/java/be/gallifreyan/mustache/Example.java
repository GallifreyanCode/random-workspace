package be.gallifreyan.mustache;

import java.util.Arrays;
import java.util.List;

import be.gallifreyan.mustache.object.Developer;
import be.gallifreyan.mustache.object.Project;

public class Example extends EmptyExample {
	private int i = 1;
	/**
	 * This developers() method is equal to {{#developers}} in the mustache template. Within this tag you can
	 *  reference global variable or getter method names of the {@link Developer} class such as {{firstName}}.
	 * Not that for this example I combined both.
	 * Every {@link Developer} returned in the list will be covered.
	 * The mustache tags can be names of .
	 */
	List<Developer> developers() {
		return Arrays.asList(
				new Developer("Kim", "Kerger", "Software Engineer", Arrays.asList(getA1("912"),
						getA2("001"))),
				new Developer("Daan", "Vleugels", "Software Engineer", Arrays.asList(getA1("912"))));
	}
	
	public String count(){
		return String.valueOf(i++);
	}
	
	public Project getA1(String code){
		return new Project(code, "The best project in the world!");
	}
	public Project getA2(String code){
		return new Project(code, "The lamest project ever");
	}
}