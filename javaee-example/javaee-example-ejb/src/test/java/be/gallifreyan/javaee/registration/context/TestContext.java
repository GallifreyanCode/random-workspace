package be.gallifreyan.javaee.registration.context;

public class TestContext
{

	private Class<?> clazz;
	private String property;
	private Class<?> inverseClazz;
	private String inverseProperty;
	private RelationType relationType;
	private CollectionTestType collectionTestType;
	private PropertyTestType propertyTestType;

	public TestContext(Class<?> clazz, String property, Class<?> inverseClazz, String inverseProperty,
			RelationType relationType)
	{
		this.clazz = clazz;
		this.property = property;
		this.inverseClazz = inverseClazz;
		this.inverseProperty = inverseProperty;
		this.relationType = relationType;
	}

	public TestContext(TestContext context)
	{
		this.clazz = context.getClazz();
		this.property = context.getProperty();
		this.inverseClazz = context.getInverseClazz();
		this.inverseProperty = context.getInverseProperty();
		this.relationType = context.getRelationType();
	}

	public Class<?> getClazz()
	{
		return clazz;
	}

	public void setClazz(Class<?> clazz)
	{
		this.clazz = clazz;
	}

	public String getProperty()
	{
		return property;
	}

	public void setProperty(String property)
	{
		this.property = property;
	}

	public Class<?> getInverseClazz()
	{
		return inverseClazz;
	}

	public void setInverseClazz(Class<?> inverseClazz)
	{
		this.inverseClazz = inverseClazz;
	}

	public String getInverseProperty()
	{
		return inverseProperty;
	}

	public void setInverseProperty(String inverseProperty)
	{
		this.inverseProperty = inverseProperty;
	}

	public RelationType getRelationType()
	{
		return relationType;
	}

	public void setRelationType(RelationType relationType)
	{
		this.relationType = relationType;
	}

	public CollectionTestType getCollectionTestType()
	{
		return collectionTestType;
	}

	public void setCollectionTestType(CollectionTestType collectionTestType)
	{
		this.collectionTestType = collectionTestType;
	}

	public PropertyTestType getPropertyTestType()
	{
		return propertyTestType;
	}

	public void setPropertyTestType(PropertyTestType propertyTestType)
	{
		this.propertyTestType = propertyTestType;
	}

	@Override
	public String toString()
	{
		return " \"" + property + "\" in class " + clazz + " , having inverse \"" + inverseProperty + "\" in class "
				+ inverseClazz + ", with test to be executed being:"
				+ (collectionTestType == null ? propertyTestType : collectionTestType);
	}

}
