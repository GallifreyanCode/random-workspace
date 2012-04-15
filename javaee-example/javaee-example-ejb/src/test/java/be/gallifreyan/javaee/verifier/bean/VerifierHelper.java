package be.gallifreyan.javaee.verifier.bean;

import static org.junit.Assert.fail;

import java.beans.*;
import java.util.*;

import org.slf4j.*;

public class VerifierHelper
{
	private static final Logger logger = LoggerFactory.getLogger(VerifierHelper.class);

	private VerifierHelper()
	{
	}

	/**
	 * @param beanClass
	 * @return
	 */
	public static Collection<Object[]> getProperties(Class<?> beanClass)
	{
		List<Object[]> list = new ArrayList<Object[]>();
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(beanClass, Object.class);
			for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors())
			{
				logger.info("Adding property {} to the test parameter.", propertyDescriptor.getName());
				list.add(new Object[] { propertyDescriptor });
			}
		}
		catch (IntrospectionException ex)
		{
			logger.error("Failed in bean introspection.", ex);
			fail("Failed to introspect bean.");
		}
		return list;
	}

}
