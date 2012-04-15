package be.gallifreyan.javaee.verifier.registration;

import static be.gallifreyan.javaee.registration.context.CollectionTestType.*;
import static be.gallifreyan.javaee.registration.context.PropertyTestType.*;
import static be.gallifreyan.javaee.registration.context.RelationType.*;

import java.util.*;

import be.gallifreyan.javaee.registration.context.*;


public class VerifierHelper
{
	private static final CollectionTestType[] collectionTestTypes = { ADD_NULL_TO_COLLECTION,
			ADD_ELEMENT_TO_COLLECTION, ADD_DUPLICATE_TO_COLLECTION, REMOVE_NULL_FROM_COLLECTION,
			REMOVE_ELEMENT_FROM_COLLECTION, REMOVE_NONEXISTENT_FROM_COLLECTION };

	private static final PropertyTestType[] propertyTestTypes = { MODIFY_FIELD_WITH_NULL, MODIFY_FIELD_WITH_NEW_VALUE,
			MODIFY_FIELD_WITH_CURRENT, CLEAR_NULL_FIELD, CLEAR_NOTNULL_FIELD };

	private VerifierHelper()
	{
	}

	public static Collection<Object[]> fetchPopulatedContexts(TestContext[] contexts)
	{
		List<Object[]> contextList = new ArrayList<Object[]>();
		for (TestContext context : contexts)
		{
			RelationType relationType = (RelationType) context.getRelationType();
			if (relationType.equals(ONE_TO_ONE) || relationType.equals(MANY_TO_ONE))
			{
				for (PropertyTestType type : propertyTestTypes)
				{
					TestContext newContext = new TestContext(context);
					newContext.setPropertyTestType(type);
					contextList.add(new Object[] { newContext });
				}
			}
			else
			{
				for (CollectionTestType type : collectionTestTypes)
				{
					TestContext newContext = new TestContext(context);
					newContext.setCollectionTestType(type);
					contextList.add(new Object[] { newContext });
				}
			}
		}
		return contextList;
	}
}
