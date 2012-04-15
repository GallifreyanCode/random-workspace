package be.gallifreyan.javaee.service.ejb;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Suite.SuiteClasses;

/**
 * An integration test suite to ensure that the embedded Glassfish container
 * used by the integration tests is started only once. This will allow tests to
 * run faster as the deployment of the application in an embedded container is
 * disk and CPU intensive - the GF embedded container is re-created for every
 * invocation of <code>EJBContainer.createEJBContainer</code> if a GF
 * installation root is not specified.
 * 
 * @author Vineet Reynolds
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ AlbumServiceIntegrationTest.class,
		GroupServiceIntegrationTest.class, PhotoServiceIntegrationTest.class,
		UserServiceIntegrationTest.class })
public class ServicesIntegrationSuite {

	/**
	 * Starts the embedded container before running the tests in the suite.
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void beforeSuite() throws Exception {
		AbstractIntegrationTest.isRunFromSuite = true;
		AbstractIntegrationTest.startup();
	}

	/**
	 * Shutdown the embedded container only after running all the tests in the
	 * suite.
	 */
	@AfterClass
	public static void afterSuite() {
		AbstractIntegrationTest.shutdown();
		AbstractIntegrationTest.isRunFromSuite = false;
	}
}
