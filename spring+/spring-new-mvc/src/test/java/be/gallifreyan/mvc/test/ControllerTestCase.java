package be.gallifreyan.mvc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import be.gallifreyan.mvc.HomeControllerTest;
import be.gallifreyan.mvc.IndexControllerTest;
import be.gallifreyan.mvc.SimpleControllerRevisitedTest;
import be.gallifreyan.mvc.SimpleControllerTest;

@RunWith(Suite.class)
@SuiteClasses	({	IndexControllerTest.class,
					HomeControllerTest.class,
					SimpleControllerTest.class,
					SimpleControllerRevisitedTest.class
				})
public class ControllerTestCase {

}
