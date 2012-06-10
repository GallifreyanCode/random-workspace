package be.gallifreyan.ws.server;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import be.gallifreyan.ws.server.test.OrderServiceEndpointITest;
import be.gallifreyan.ws.server.test.UserServiceEndpointITest;

@RunWith(Suite.class)
@SuiteClasses	({	OrderServiceEndpointITest.class,
					UserServiceEndpointITest.class
				})
public class EndpointITestCase {

}
