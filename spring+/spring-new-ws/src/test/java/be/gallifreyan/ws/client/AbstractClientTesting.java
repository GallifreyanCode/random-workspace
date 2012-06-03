package be.gallifreyan.ws.client;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.gallifreyan.ws.client.config.WSClientTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =	{	WSClientTestConfig.class
								})
public abstract class AbstractClientTesting {
}
