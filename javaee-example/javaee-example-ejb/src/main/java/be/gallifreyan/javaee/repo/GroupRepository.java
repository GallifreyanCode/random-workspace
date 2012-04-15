package be.gallifreyan.javaee.repo;

import java.util.List;

import javax.ejb.*;
import javax.persistence.*;

import be.gallifreyan.javaee.entity.Group;
import be.gallifreyan.javaee.service.GenericRepository;

@Stateless
@LocalBean
public class GroupRepository implements GenericRepository<Group, String> {

	@PersistenceContext
	private EntityManager em;

	public GroupRepository() {
	}

	GroupRepository(EntityManager em) {
		this.em = em;
	}

	@Override
	public Group create(Group group) {
		em.persist(group);
		return group;
	}

	@Override
	public Group modify(Group group) {
		em.find(Group.class, group.getGroupId());
		Group mergedGroup = em.merge(group);
		return mergedGroup;
	}

	@Override
	public void delete(Group group) {
		Group mergedGroup = em.merge(group);
		em.remove(mergedGroup);
	}

	@Override
	public Group findById(String groupId) {
		Group group = em.find(Group.class, groupId);
		return group;
	}

	@Override
	public List<Group> findAll() {
		TypedQuery<Group> allGroupsQuery = em.createNamedQuery(
				"Group.findAllGroups", Group.class);
		List<Group> result = allGroupsQuery.getResultList();
		return result;
	}
}
