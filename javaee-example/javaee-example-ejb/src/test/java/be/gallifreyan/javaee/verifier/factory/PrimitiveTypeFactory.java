package be.gallifreyan.javaee.verifier.factory;

public class PrimitiveTypeFactory implements ITypeFactory
{
	
	/**
	 * Creates an instance of a primitive class.
	 * Compares the argument class against the class in java.lang and the primitive type before creating the instance.  
	 * @param primitiveType A Class object representing a primitive type
	 * @return A primitive value corresponding to the primitive type
	 */
	@Override
	public Object create(Class<?> primitiveType)
	{
		Object mockValue = null;
		if(primitiveType.equals(Integer.class) || primitiveType.equals(int.class))
		{
			mockValue = Integer.MIN_VALUE;
		}
		else if(primitiveType.equals(Byte.class) || primitiveType.equals(byte.class))
		{
			mockValue = Byte.MIN_VALUE;
		}
		else if(primitiveType.equals(Short.class) || primitiveType.equals(short.class))
		{
			mockValue = Short.MIN_VALUE;
		}
		else if(primitiveType.equals(Long.class) || primitiveType.equals(long.class))
		{
			mockValue = Long.MIN_VALUE;
		}
		else if(primitiveType.equals(Character.class) || primitiveType.equals(char.class))
		{
			mockValue = '0';
		}
		else if(primitiveType.equals(Float.class) || primitiveType.equals(float.class))
		{
			mockValue = Float.MIN_VALUE;
		}
		else if(primitiveType.equals(Double.class) || primitiveType.equals(double.class))
		{
			mockValue = Double.MIN_VALUE;
		}
		else if(primitiveType.equals(Boolean.class) ||  primitiveType.equals(boolean.class))
		{
			mockValue = Boolean.FALSE;
		}
		return mockValue;
	}
}