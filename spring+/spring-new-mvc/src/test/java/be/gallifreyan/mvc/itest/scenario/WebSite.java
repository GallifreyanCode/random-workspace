package be.gallifreyan.mvc.itest.scenario;

import org.springframework.stereotype.Component;

import be.gallifreyan.mvc.itest.object.IndexPage;
import be.gallifreyan.mvc.itest.object.PageObject;

@Component
public class WebSite extends PageObject {

	public IndexPage openHomePage() {
		return openAs("http://localhost:9773/", IndexPage.class);
	}
}
