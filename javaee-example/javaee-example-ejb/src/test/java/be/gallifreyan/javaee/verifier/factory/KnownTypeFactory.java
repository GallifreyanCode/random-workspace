package be.gallifreyan.javaee.verifier.factory;

public class KnownTypeFactory implements ITypeFactory {

	/**
	 * Creates an instance of a class that is known. Support for other known
	 * classes have to be added here.
	 * 
	 * @param clazz
	 *            The name of the class to be created
	 * @return A mock instance of the class.
	 */
	@Override
	public Object create(Class<?> type) {
		Object mockValue = null;
		if (type.equals(String.class)) {
			mockValue = new String("MockValue");
		} else if (type.equals(java.util.Date.class)) {
			mockValue = new java.util.Date();
		} else if (type.equals(java.sql.Date.class)) {
			mockValue = new java.sql.Date(0);
		} else if (type.equals(java.sql.Time.class)) {
			mockValue = new java.sql.Time(0);
		} else if (type.equals(java.sql.Timestamp.class)) {
			mockValue = new java.sql.Timestamp(0);
		}
		return mockValue;
	}
}
