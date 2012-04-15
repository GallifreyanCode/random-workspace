package be.gallifreyan.javaee.verifier.factory;

public class NullTypeFactory implements ITypeFactory {

	@Override
	public Object create(Class<?> propertyType) {
		return null;
	}
}
