package be.gallifreyan.mvc;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import be.gallifreyan.mvc.test.AbstractControllerTest;
import be.gallifreyan.mvc.test.MockDataFactory;
import be.gallifreyan.persistence.service.DeveloperService;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest extends AbstractControllerTest {
	@Mock
	private DeveloperService developerService;

	@Before
	public void setUp() {
		Mockito.when(developerService.findAll()).thenReturn(
				MockDataFactory.createMockDevelopers());
	}

	@Test
	public void testDevelopersRequest() throws Exception {
		HomeController homeController = new HomeController();
		homeController.setDeveloperService(developerService);
		MockMvcBuilders.standaloneSetup(homeController)
				.setViewResolvers(viewResolver).build()
				.perform(get("/developers")).andExpect(status().isOk())
				.andExpect(isPage("home"));
	}
}
