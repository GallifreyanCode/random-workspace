package be.gallifreyan.itest;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.gallifreyan.persistence.entity.Developer;
import be.gallifreyan.persistence.entity.User;
import be.gallifreyan.persistence.service.DeveloperService;
import be.gallifreyan.persistence.service.UserService;

public abstract class AbstractJPAITest {
	private static final Logger logger = LoggerFactory.getLogger(AbstractJPAITest.class);
	@Inject
	DeveloperService developerService;
	@Inject
	UserService userService;

	@Before
	public void setUp() {
		assertNotNull(logger);
		assertNotNull(userService);
		logger.debug("UserService: " + userService);
		assertNotNull(developerService);
		logger.debug("DeveloperService: " + developerService);
	}

	@Test
	public void testSaveDeveloper() {
		logger.debug("testSaveDeveloper");
		Developer sample = new Developer();
		sample.setId(1L);
		sample.setName("Developer1");
		developerService.save(sample);
		assertNotNull("id should not be null", sample.getId());
		//assertEquals(sample.getId(), developerService.findAll().get(0).getId());
		Developer developer = developerService.findByName("Developer1");
		assertNotNull(developer);
		logger.debug("Developer found: " + developer.getName());
	}
	
	@Test
	public void testSaveUser() {
		logger.debug("testSaveUser");
		User sample = new User();
		sample.setId(1L);
		sample.setName("User1");
		userService.save(sample);
		assertNotNull("id should not be null", sample.getId());
		//assertEquals(sample.getId(), userService.findAll().get(0).getId());
		User user = userService.findByName("User1");
		assertNotNull(user);
		logger.debug("User found: " + user.getName());
	}
}
