package be.gallifreyan.mvc;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import be.gallifreyan.mvc.test.AbstractControllerTest;

public class SimpleControllerTest extends AbstractControllerTest {

	@Test
	public void testSimpleRequest() throws Exception {
		MockMvcBuilders.standaloneSetup(new SimpleController()).build()
	    .perform(get("/simple"))
	        .andExpect(status().isOk())
	        .andExpect(content().string("Hello world!"));
	}
}
