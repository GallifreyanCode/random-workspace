package be.gallifreyan.javaee.verifier.registration;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collection;


import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

class CollectionAddNullVerifier extends MutualRegistrationVerifier
{
	private static final Logger logger = LoggerFactory.getLogger(CollectionAddNullVerifier.class);

	CollectionAddNullVerifier(TestContext context, ITypeFactory typeFactory)
	{
		super(context, typeFactory);
	}

	@Override
	public void verify()
	{
		Class<?> clazz = context.getClazz();
		Object system = typeFactory.create(clazz);
		Method addToCollection = getContextMethod(Operation.COLLECTION_ADD);
		Object collectionElement = null;
		Object[] arguments = new Object[] { collectionElement };
		try
		{
			addToCollection.invoke(system, arguments);

			Method readMethod = getPropertyReadMethod();
			Object property = readMethod.invoke(system, (Object[]) null);
			Collection<?> collection = (Collection<?>) property;
			assertTrue("The collection should be empty", collection.size() == 0);
			assertTrue("The collection should not contain null.", !collection.contains(collectionElement));

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
