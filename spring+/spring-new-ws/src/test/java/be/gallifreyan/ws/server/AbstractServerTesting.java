package be.gallifreyan.ws.server;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.gallifreyan.ws.server.config.WSServerTestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {	WSServerTestConfig.class
								})
public abstract class AbstractServerTesting {
}
