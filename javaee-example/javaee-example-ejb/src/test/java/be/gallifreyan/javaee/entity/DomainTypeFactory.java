package be.gallifreyan.javaee.entity;

import be.gallifreyan.javaee.verifier.factory.ITypeFactory;

public class DomainTypeFactory implements ITypeFactory
{
	@Override
	public Object create(Class<?> clazz)
	{
		if(clazz.equals(Photo.class))
			return new Photo();
		else if(clazz.equals(Group.class))
			return new Group();
		else if(clazz.equals(User.class))
			return new User();
		else if(clazz.equals(Album.class))
			return new Album();
		else
			return null;
	}
}