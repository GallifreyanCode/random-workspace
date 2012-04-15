package be.gallifreyan.javaee.verifier.registration;

import java.beans.*;
import java.lang.reflect.Method;


import org.slf4j.*;

import be.gallifreyan.javaee.registration.context.TestContext;
import be.gallifreyan.javaee.verifier.factory.*;

public abstract class MutualRegistrationVerifier
{
	private static final Logger logger = LoggerFactory.getLogger(MutualRegistrationVerifier.class);

	protected TestContext context;

	protected ITypeFactory typeFactory;

	protected MutualRegistrationVerifier(TestContext context, ITypeFactory typeFactory)
	{
		this.context = context;
		if (typeFactory == null)
		{
			this.typeFactory = new NullTypeFactory();
		}
		else
		{
			this.typeFactory = typeFactory;
		}
	}

	public static MutualRegistrationVerifier forContext(TestContext context, ITypeFactory typeFactory)
	{
		if (context.getCollectionTestType() != null)
		{
			switch (context.getCollectionTestType())
			{
				case ADD_NULL_TO_COLLECTION:
					return new CollectionAddNullVerifier(context, typeFactory);
				case ADD_ELEMENT_TO_COLLECTION:
					return new CollectionAddElementVerifier(context, typeFactory);
				case ADD_DUPLICATE_TO_COLLECTION:
					return new CollectionAddDuplicateVerifier(context, typeFactory);
				case REMOVE_NULL_FROM_COLLECTION:
					return new CollectionRemoveNullVerifier(context, typeFactory);
				case REMOVE_ELEMENT_FROM_COLLECTION:
					return new CollectionRemoveElementVerifier(context, typeFactory);
				case REMOVE_NONEXISTENT_FROM_COLLECTION:
					return new CollectionRemoveNonexistentElementVerifier(context, typeFactory);
			}
		}
		if (context.getPropertyTestType() != null)
		{
			switch (context.getPropertyTestType())
			{
				case MODIFY_FIELD_WITH_NULL:
					return new PropertyModifyNullVerifier(context, typeFactory);
				case MODIFY_FIELD_WITH_NEW_VALUE:
					return new PropertyModifyWithNewValueVerifier(context, typeFactory);
				case MODIFY_FIELD_WITH_CURRENT:
					return new PropertyModifyWithCurrentVerifier(context, typeFactory);
				case CLEAR_NOTNULL_FIELD:
					return new PropertyClearNotNullVerifier(context, typeFactory);
				case CLEAR_NULL_FIELD:
					return new PropertyClearNullVerifier(context, typeFactory);
			}
		}
		return null;
	}

	public abstract void verify();

	/**
	 * 
	 * @param operation
	 * @return
	 */
	protected Method getContextMethod(Operation operation)
	{
		String expectedMethodName = operation + getPropertyNameInTitleCase(context.getProperty());

		Method futureMethod = null;
		for (Method method : context.getClazz().getMethods())
		{
			if (method.getName().equals(expectedMethodName))
			{
				futureMethod = method;
			}
		}

		return futureMethod;
	}

	protected Method getPropertyReadMethod()
	{
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(context.getClazz(), Object.class);
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors())
			{
				if (descriptor.getName().equals(context.getProperty()))
				{
					return descriptor.getReadMethod();
				}
			}
		}
		catch (IntrospectionException introEx)
		{
			logger.error("Failed to introspect the SUT class to obtain the read method.", introEx);
		}
		return null;
	}

	protected Method getInversePropertyReadMethod()
	{
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(context.getInverseClazz(), Object.class);
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors())
			{
				if (descriptor.getName().equals(context.getInverseProperty()))
				{
					return descriptor.getReadMethod();
				}
			}
		}
		catch (IntrospectionException introEx)
		{
			logger.error("Failed to introspect the Inverse class to obtain the inverse read method.", introEx);
		}
		return null;
	}

	protected String getPropertyNameInTitleCase(String propertyName)
	{
		char[] propertyCharacters = propertyName.toCharArray();
		propertyCharacters[0] = Character.toTitleCase(propertyCharacters[0]);
		String propertyInTitleCase = new String(propertyCharacters);
		return propertyInTitleCase;
	}

	public enum Operation
	{
		COLLECTION_ADD("addTo"), COLLECTION_REMOVE("removeFrom"), PROPERTY_MODIFY("modify"), PROPERTY_CLEAR("clear");

		String value;

		Operation(String value)
		{
			this.value = value;
		}

		public String toString()
		{
			return value;
		}

	}
}
