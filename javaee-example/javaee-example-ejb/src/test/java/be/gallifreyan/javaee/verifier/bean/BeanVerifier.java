package be.gallifreyan.javaee.verifier.bean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;


import org.slf4j.*;

import be.gallifreyan.javaee.verifier.factory.*;

public class BeanVerifier<T>
{
	private static final Logger logger = LoggerFactory.getLogger(BeanVerifier.class);
	private static final int MOCK_ARRAY_SIZE = 1000;

	private Class<T> type;
	private ITypeFactory typeFactory;
	private ITypeFactory primitiveTypeFactory;
	private ITypeFactory knownTypeFactory;

	private PropertyDescriptor descriptor;

	private BeanVerifier(Class<T> type, PropertyDescriptor descriptor, ITypeFactory mockFactory)
	{
		this.type = type;
		this.descriptor = descriptor;
		if (mockFactory == null)
		{
			this.typeFactory = new NullTypeFactory();
		}
		else
		{
			this.typeFactory = mockFactory;
		}
		this.primitiveTypeFactory = new PrimitiveTypeFactory();
		this.knownTypeFactory = new KnownTypeFactory();
	}

	/**
	 * Creates a new BeanVerifier to verify a Java Bean class.
	 * 
	 * @param type
	 *            The type representing the Java Bean to be verified
	 * @param descriptor
	 *            The descriptor of the bean property to be verified
	 * @param typeFactory
	 *            The factory to be used to create instances of bean properties.
	 * @return A new BeanVerifier instance
	 */
	public static <T> BeanVerifier<T> forProperty(Class<T> type, PropertyDescriptor descriptor, ITypeFactory typeFactory)
	{
		return new BeanVerifier<T>(type, descriptor, typeFactory);
	}

	/**
	 * Verifies the behavior of getter and setter methods of Java Beans. This
	 * method introspects a Java class to get it's Java Bean properties. It
	 * iterates through all properties, invoking the setter methods with mock
	 * instances of parameters and verifies that the associated getter method
	 * will return the created mock instance.
	 */
	public void verify()
	{
		try
		{
			Class<?> propertyType = descriptor.getPropertyType();
			// delegate first to the provided type factory to create a type
			// instance
			Object propertyInstance = typeFactory.create(propertyType);
			if (propertyInstance == null)
			{
				propertyInstance = createInstanceOfType(propertyType);
			}
			if (propertyInstance == null)
			{
				// Use Mockito to mock the property
				// TODO Use the Reflection API's Proxy instead
				propertyInstance = mock(propertyType);
			}
			if (propertyInstance == null)
			{
				fail("Failed to create a mock object of type" + propertyType.getName());
			}

			// Setup
			Object system = typeFactory.create(type);
			if (system == null)
			{
				system = type.newInstance();
			}

			// Execute
			Method writeMethod = descriptor.getWriteMethod();
			Method readMethod = descriptor.getReadMethod();
			writeMethod.invoke(system, propertyInstance);
			Object actualObject = readMethod.invoke(system, (Object[]) null);

			// Verify
			assertEquals(String.format("Verification failed for property %s", descriptor.getName()), propertyInstance,
					actualObject);
		}
		catch (IllegalAccessException illegalEx)
		{
			logger.error(null, illegalEx);
			fail("Verification failed for property:" + descriptor.getName());
		}
		catch (InstantiationException instanceEx)
		{
			logger.error(null, instanceEx);
			fail("Verification failed for property:" + descriptor.getName());
		}
		catch (InvocationTargetException invokeEx)
		{
			logger.error(null, invokeEx);
			fail("Verification failed for property:" + descriptor.getName());
		}
	}

	/**
	 * Delegates the creation of mock instances of types to other methods.
	 * Supported types include primitives, arrays and known/supported classes.
	 * 
	 * @param propertyType
	 *            The type of the class whose instance needs to be created.
	 * @return An instance of the type
	 */
	private Object createInstanceOfType(Class<?> propertyType)
	{
		Object typeInstance = null;
		if (propertyType.isArray())
		{
			typeInstance = createArray(propertyType);
		}
		else if (propertyType.isPrimitive())
		{
			typeInstance = primitiveTypeFactory.create(propertyType);
		}
		else
		{
			typeInstance = knownTypeFactory.create(propertyType);
		}
		return typeInstance;
	}

	/**
	 * Creates a mock array filled with mock instances of component type of the
	 * array. This population of the array is done recursively.
	 * 
	 * @param clazz
	 *            The class representing the array type
	 * @return The mock array
	 */
	private Object createArray(Class<?> clazz)
	{
		Object mockValue;
		Class<?> componentType = clazz.getComponentType();
		mockValue = createInstanceOfType(componentType);
		Object array = Array.newInstance(componentType, MOCK_ARRAY_SIZE);
		for (int ctr = 0; ctr < MOCK_ARRAY_SIZE; ctr++)
		{
			Array.set(array, ctr, mockValue);
		}
		mockValue = array;
		return mockValue;
	}
}
