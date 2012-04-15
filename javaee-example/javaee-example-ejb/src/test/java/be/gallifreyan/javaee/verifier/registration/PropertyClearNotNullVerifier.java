package be.gallifreyan.javaee.verifier.registration;

import static be.gallifreyan.javaee.registration.context.RelationType.MANY_TO_ONE;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collection;


import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

class PropertyClearNotNullVerifier extends MutualRegistrationVerifier
{
	private static final Logger logger = LoggerFactory.getLogger(PropertyClearNotNullVerifier.class);

	PropertyClearNotNullVerifier(TestContext context, ITypeFactory typeFactory)
	{
		super(context, typeFactory);
	}

	@Override
	public void verify()
	{
		Class<?> clazz = context.getClazz();
		Object system = typeFactory.create(clazz);
		Method modifyProperty = getContextMethod(Operation.PROPERTY_MODIFY);
		Method clearProperty = getContextMethod(Operation.PROPERTY_CLEAR);

		Object property = typeFactory.create(context.getInverseClazz());
		Object[] arguments = new Object[] { property };
		Object[] clearArguments = new Object[] {};
		try
		{
			modifyProperty.invoke(system, arguments);

			clearProperty.invoke(system, clearArguments);

			Method readMethod = getPropertyReadMethod();
			Object actualProperty = readMethod.invoke(system, (Object[]) null);

			assertNull(actualProperty);

			Method inverseReadMethod = getInversePropertyReadMethod();
			Object inverseProperty = inverseReadMethod.invoke(property, (Object[]) null);
			if (context.getRelationType().equals(MANY_TO_ONE))
			{
				Collection<?> inverseCollection = (Collection<?>) inverseProperty;
				assertTrue("The inverse collection must not contain any element", inverseCollection.size() == 0);
				assertTrue("The inverse collection must not contain the system.", !inverseCollection.contains(system));
			}
			else
			{
				assertNull(inverseProperty);
			}
		}
		catch (Exception ex)
		{
			logger.error("Failed to invoke the method under test in the SUT.", ex);
			fail("Failed to invoke the method under test in the SUT.");
		}
	}

}
