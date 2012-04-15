package be.gallifreyan.javaee.entity;

import java.beans.PropertyDescriptor;
import java.util.Collection;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.*;

import be.gallifreyan.javaee.verifier.bean.*;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

@RunWith(Parameterized.class)
public class UserBeanVerifier
{
	private static final Class<User> TEST_TYPE = User.class;

	static final Logger logger = LoggerFactory.getLogger(UserBeanVerifier.class);

	private ITypeFactory typeFactory = new DomainTypeFactory();
	private PropertyDescriptor descriptor;

	@Parameters
	public static Collection<Object[]> properties()
	{
		return VerifierHelper.getProperties(TEST_TYPE);
	}

	public UserBeanVerifier(PropertyDescriptor descriptor)
	{
		this.descriptor = descriptor;
	}

	@Test
	public void testProperty() throws Exception
	{
		logger.info("Verifying property {} in bean {}", descriptor.getName(), TEST_TYPE);
		BeanVerifier<User> verifier = BeanVerifier.forProperty(TEST_TYPE, descriptor, typeFactory);
		verifier.verify();
	}
}
