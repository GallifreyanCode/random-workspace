package be.gallifreyan.javaee.entity;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GroupBeanVerifier.class, GroupEqualsAndConstructorVerifier.class, GroupMutualRegistrationVerifier.class })
public class GroupSuite
{

}
