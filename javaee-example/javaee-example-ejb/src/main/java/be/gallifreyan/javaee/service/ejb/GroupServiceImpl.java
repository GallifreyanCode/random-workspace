package be.gallifreyan.javaee.service.ejb;

import javax.ejb.*;

import be.gallifreyan.javaee.entity.Group;
import be.gallifreyan.javaee.repo.GroupRepository;

@Stateless
@EJB(name = "java:global/galleria/galleria-ejb/GroupService", beanInterface = GroupService.class)
public class GroupServiceImpl implements GroupService {

	private static final String REGISTERED_USERS_GROUP_NAME = "RegisteredUsers";

	@EJB
	private GroupRepository groupRepository;

	public Group getOrCreateRegisteredUsersGroup() {
		Group defaultGroup = groupRepository
				.findById(REGISTERED_USERS_GROUP_NAME);
		if (defaultGroup == null) {
			defaultGroup = createRegisteredUsersGroup();
			return defaultGroup;
		} else {
			return defaultGroup;
		}
	}

	private Group createRegisteredUsersGroup() {
		Group group = new Group(REGISTERED_USERS_GROUP_NAME);
		groupRepository.create(group);
		return group;
	}
}
