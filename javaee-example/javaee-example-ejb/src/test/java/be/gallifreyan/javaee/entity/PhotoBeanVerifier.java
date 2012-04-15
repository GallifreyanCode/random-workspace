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
public class PhotoBeanVerifier
{
	private static final Class<Photo> TEST_TYPE = Photo.class;

	private static final Logger logger = LoggerFactory.getLogger(PhotoBeanVerifier.class);

	private ITypeFactory typeFactory = new DomainTypeFactory();
	private PropertyDescriptor descriptor;

	@Parameters
	public static Collection<Object[]> properties()
	{
		return VerifierHelper.getProperties(TEST_TYPE);
	}

	public PhotoBeanVerifier(PropertyDescriptor descriptor)
	{
		this.descriptor = descriptor;
	}

	@Test
	public void testProperty() throws Exception
	{
		logger.info("Verifying property {} in bean {}", descriptor.getName(), TEST_TYPE);
		BeanVerifier<Photo> verifier = BeanVerifier.forProperty(TEST_TYPE, descriptor, typeFactory);
		verifier.verify();
	}
}
