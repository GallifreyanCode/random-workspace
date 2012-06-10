package be.gallifreyan.mvc.test;

import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;

import org.junit.BeforeClass;
import org.springframework.test.web.server.ResultMatcher;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class AbstractControllerTest {
	protected static InternalResourceViewResolver viewResolver ;
	protected static final String PAGE_PATH = "/WEB-INF/views/";
	protected static final String PAGE_EXT = ".jsp";
	
	@BeforeClass
	public static void setUpClass() {
		viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix(PAGE_PATH);
		viewResolver.setSuffix(PAGE_EXT);
	}
	
	protected ResultMatcher isPage(String pageName) {
		return forwardedUrl(PAGE_PATH + pageName + PAGE_EXT);
	}
}
