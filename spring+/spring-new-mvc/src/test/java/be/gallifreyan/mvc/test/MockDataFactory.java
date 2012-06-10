package be.gallifreyan.mvc.test;

import java.util.ArrayList;
import java.util.List;

import be.gallifreyan.persistence.entity.Developer;

public class MockDataFactory {

	public static List<Developer> createMockDevelopers() {
		List<Developer> mockDevelopers = new ArrayList<Developer>();
		Developer mockDeveloper = new Developer();
		mockDeveloper.setName("MockDev");
		mockDevelopers.add(mockDeveloper);
		return mockDevelopers;
	}
}
