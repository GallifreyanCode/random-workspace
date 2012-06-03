package be.gallifreyan.itest.abs;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceEclipseLinkConfig;
import be.gallifreyan.config.profile.MemoryDataConfig;
import be.gallifreyan.config.profile.StandaloneDataConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {	ContextConfig.class,
									PersistenceEclipseLinkConfig.class,
									StandaloneDataConfig.class,
									MemoryDataConfig.class
								})
@ActiveProfiles("dev")
public abstract class AbstractEclipseLinkTesting {

}
