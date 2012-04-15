package be.gallifreyan.javaee.verifier.registration;

import static be.gallifreyan.javaee.registration.context.RelationType.MANY_TO_MANY;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Collection;


import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

class CollectionRemoveNonexistentElementVerifier extends MutualRegistrationVerifier
{
	private static final Logger logger = LoggerFactory.getLogger(CollectionRemoveNonexistentElementVerifier.class);

	CollectionRemoveNonexistentElementVerifier(TestContext context, ITypeFactory typeFactory)
	{
		super(context, typeFactory);
	}

	@Override
	public void verify()
	{
		Class<?> clazz = context.getClazz();
		Object system = typeFactory.create(clazz);
		Method addMethodToCollection = getContextMethod(Operation.COLLECTION_ADD);
		Method removeMethodFromCollection = getContextMethod(Operation.COLLECTION_REMOVE);

		Object collectionElement = typeFactory.create(context.getInverseClazz());
		Object[] arguments = new Object[] { collectionElement };
		try
		{
			addMethodToCollection.invoke(system, arguments);
			removeMethodFromCollection.invoke(system, arguments);

			removeMethodFromCollection.invoke(system, arguments);

			Method readMethod = getPropertyReadMethod();
			Object property = readMethod.invoke(system, (Object[]) null);
			Collection<?> collection = (Collection<?>) property;
			assertTrue("The collection must not contain any elements", collection.size() == 0);
			assertTrue("The collection must not contain the original element.", !collection.contains(collectionElement));

			Method inverseReadMethod = getInversePropertyReadMethod();
			Object inverseProperty = inverseReadMethod.invoke(collectionElement, (Object[]) null);
			if (context.getRelationType().equals(MANY_TO_MANY))
			{
				Collection<?> inverseCollection = (Collection<?>) inverseProperty;
				assertTrue("The inverse collection must not contain any elements.", inverseCollection.size() == 0);
				assertTrue("The inverse collection must not contain the system.", !inverseCollection.contains(system));
			}
			else
			{
				// Relation type can only be One-To-Many here for this test.
				assertNull("The inverse property in the other class must be null.", inverseProperty);
			}
		}
		catch (Exception ex)
		{
			logger.error("Failed to invoke the method under test in the SUT.", ex);
			fail("Failed to invoke the method under test in the SUT.");
		}
	}

}
