package be.gallifreyan.mvc.itest.scenario;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import be.gallifreyan.mvc.itest.object.IndexPage;

import cucumber.annotation.en.Given;
import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

public class IndexPageSteps {

	@Inject
	WebSite webSite;
	private IndexPage homePage;

	@Given("I have a skeleton web application")
	public void haveSkeletonWebApplication() {
		assertNotNull(webSite);
	}

	@When("I open the home page")
	public void openHomePage() {
		homePage = webSite.openHomePage();
	}

	@Then("the page message should be \"(.*)\"")
	public void shouldHavePageHeading(String expectedMessage) {
		assertTrue(homePage.hasPageMessage(expectedMessage));
	}
}
