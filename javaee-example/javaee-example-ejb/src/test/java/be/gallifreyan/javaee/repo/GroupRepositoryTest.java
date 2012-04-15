package be.gallifreyan.javaee.repo;

import static be.gallifreyan.javaee.repo.UserRepositoryTest.TEST_PASSWORD;
import static be.gallifreyan.javaee.repo.UserRepositoryTest.TEST_USER_ID;
import static org.junit.Assert.*;

import java.util.List;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.gallifreyan.javaee.entity.Group;
import be.gallifreyan.javaee.entity.User;
import be.gallifreyan.javaee.repo.GroupRepository;

public class GroupRepositoryTest extends AbstractRepositoryTest
{
	private static final Logger logger = LoggerFactory.getLogger(GroupRepositoryTest.class);

	static final String TEST_GROUP_ID = "GROUP #1";
	
	private GroupRepository repository;
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		repository = new GroupRepository(em);
	}
	
	@Test
	public void testCreateGroup() throws Exception
	{
		Group group  = new Group(TEST_GROUP_ID);
		
		//Execute
		repository.create(group);
		
		//Verify
		em.flush();
		em.clear();
		Group actualGroup = em.find(Group.class, TEST_GROUP_ID);
		assertEquals(group, actualGroup);
	}
	
	@Test
	public void testModifyGroupAddUser() throws Exception
	{
		//Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(user);
		
		Group group = new Group(TEST_GROUP_ID);
		em.persist(group);
		
		em.flush();
		em.clear();
		logger.info("Flushed the persistence context after creating the user and group.");
		
		//Execute
		group.addToUsers(user);
		group = repository.modify(group);
		em.flush();
		em.clear();
		logger.info("Flushed the persistence context after attaching the user to the group.");
		
		//Verify
		Group actualGroup = em.find(Group.class, TEST_GROUP_ID);
		assertEquals(group, actualGroup);
		assertTrue(actualGroup.getUsers().contains(user));
	}
	
	@Test
	public void testDeleteGroup() throws Exception
	{
		//Setup
		Group group = new Group(TEST_GROUP_ID);
		em.persist(group);
		em.flush();
		em.clear();
		
		//Execute
		repository.delete(group);
		em.flush();
		em.clear();
		
		//Verify
		Group actualGroup = em.find(Group.class, TEST_GROUP_ID);
		assertNull(actualGroup);
	}
	
	@Test
	public void testFindGroupById() throws Exception
	{
		//Setup
		Group group = new Group(TEST_GROUP_ID);
		em.persist(group);
		em.flush();
		em.clear();
		
		//Execute
		Group actualGroup = repository.findById(TEST_GROUP_ID);
		
		//Verify
		assertEquals(group, actualGroup);
	}
	
	@Test
	public void testFindAllGroups() throws Exception
	{
		//Setup
		Group firstGroup = new Group(TEST_GROUP_ID);
		Group secondGroup = new Group("Group #2");
		em.persist(firstGroup);
		em.persist(secondGroup);
		em.flush();
		em.clear();
		
		//Execute
		List<Group> groups = repository.findAll();
		
		//Verify
		assertTrue(groups.contains(firstGroup));
		assertTrue(groups.contains(secondGroup));
	}
	
}
