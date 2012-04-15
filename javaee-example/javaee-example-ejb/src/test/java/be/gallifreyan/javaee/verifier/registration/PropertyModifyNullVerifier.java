package be.gallifreyan.javaee.verifier.registration;

import static org.junit.Assert.*;

import java.lang.reflect.Method;


import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

class PropertyModifyNullVerifier extends MutualRegistrationVerifier
{
	private static final Logger logger = LoggerFactory.getLogger(PropertyModifyNullVerifier.class);

	PropertyModifyNullVerifier(TestContext context, ITypeFactory typeFactory)
	{
		super(context, typeFactory);
	}

	@Override
	public void verify()
	{
		Class<?> clazz = context.getClazz();
		Object system = typeFactory.create(clazz);
		Method modifyProperty = getContextMethod(Operation.PROPERTY_MODIFY);

		Object[] arguments = new Object[] { null };
		try
		{
			modifyProperty.invoke(system, arguments);

			Method readMethod = getPropertyReadMethod();
			Object actualProperty = readMethod.invoke(system, (Object[]) null);

			assertNull(actualProperty);

			// Cannot verify anything on the inverse-side as there is no inverse
			// object whose state is to be verified.
		}
		catch (Exception ex)
		{
			logger.error("Failed to invoke the method under test in the SUT.", ex);
			fail("Failed to invoke the method under test in the SUT.");
		}
	}

}
