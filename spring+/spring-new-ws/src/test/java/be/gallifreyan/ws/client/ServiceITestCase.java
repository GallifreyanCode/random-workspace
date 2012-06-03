package be.gallifreyan.ws.client;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import be.gallifreyan.ws.client.test.OrderServiceClientITest;
import be.gallifreyan.ws.client.test.ProfileServiceClientITest;

@RunWith(Suite.class)
@SuiteClasses	({	OrderServiceClientITest.class,
					ProfileServiceClientITest.class
				})
public class ServiceITestCase {

}
