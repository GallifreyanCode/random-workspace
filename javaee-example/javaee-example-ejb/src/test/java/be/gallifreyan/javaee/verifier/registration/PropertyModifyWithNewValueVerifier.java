package be.gallifreyan.javaee.verifier.registration;

import static be.gallifreyan.javaee.registration.context.RelationType.MANY_TO_ONE;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collection;


import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

class PropertyModifyWithNewValueVerifier extends MutualRegistrationVerifier
{
	private static final Logger logger = LoggerFactory.getLogger(PropertyModifyWithNewValueVerifier.class);

	PropertyModifyWithNewValueVerifier(TestContext context, ITypeFactory typeFactory)
	{
		super(context, typeFactory);
	}

	@Override
	public void verify()
	{
		Class<?> clazz = context.getClazz();
		Object system = typeFactory.create(clazz);
		Method modifyProperty = getContextMethod(Operation.PROPERTY_MODIFY);

		Object property = typeFactory.create(context.getInverseClazz());
		Object[] arguments = new Object[] { property };
		try
		{
			modifyProperty.invoke(system, arguments);

			Method readMethod = getPropertyReadMethod();
			Object actualProperty = readMethod.invoke(system, (Object[]) null);

			assertNotNull(actualProperty);
			assertEquals(property, actualProperty);

			Method inverseReadMethod = getInversePropertyReadMethod();
			Object inverseProperty = inverseReadMethod.invoke(property, (Object[]) null);
			if (context.getRelationType().equals(MANY_TO_ONE))
			{
				Collection<?> inverseCollection = (Collection<?>) inverseProperty;
				assertTrue("The inverse collection must contain one element", inverseCollection.size() == 1);
				assertTrue("The inverse collection must contain the system.", inverseCollection.contains(system));
			}
			else
			{
				assertEquals(system, inverseProperty);
			}
		}
		catch (Exception ex)
		{
			logger.error("Failed to invoke the method under test in the SUT.", ex);
			fail("Failed to invoke the method under test in the SUT.");
		}
	}

}
