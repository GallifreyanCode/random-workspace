package be.gallifreyan.javaee.service.ejb;


import javax.ejb.Local;

import be.gallifreyan.javaee.entity.Group;

@Local
public interface GroupService
{
	public Group getOrCreateRegisteredUsersGroup();
}
