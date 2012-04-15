package be.gallifreyan.javaee.service.ejb;

import static org.junit.Assert.*;

import org.junit.*;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.Group;

public class GroupServiceIntegrationTest extends AbstractIntegrationTest
{
	private static final Logger logger = LoggerFactory.getLogger(GroupServiceIntegrationTest.class);
	private GroupService groupService;

	@BeforeClass
	public static void beforeClass() throws Exception
	{
		logger.info("Entering beforeClass method of {}", GroupServiceIntegrationTest.class);
		AbstractIntegrationTest.setUpBeforeClass();
	}

	@AfterClass
	public static void afterClass() throws Exception
	{
		logger.info("Entering afterClass method of {}", GroupServiceIntegrationTest.class);
		AbstractIntegrationTest.tearDownAfterClass();
	}

	@Override
	public void setUp() throws Exception
	{
		logger.info("Entering setUp of method {}", testMethod.getMethodName());
		super.setUp();
		groupService = (GroupService) context.lookup("java:global/galleria/galleria-ejb/GroupService");
	}

	@Override
	public void tearDown() throws Exception
	{
		logger.info("Entering setUp of method {}", testMethod.getMethodName());
		super.tearDown();
	}

	@Test
	public void testCreateDefaultGroup() throws Exception
	{
		Group registeredUsersGroup = groupService.getOrCreateRegisteredUsersGroup();
		assertNotNull(registeredUsersGroup);
	}

	@Test
	public void testGetDefaultGroup() throws Exception
	{
		Group registeredUsersGroup = groupService.getOrCreateRegisteredUsersGroup();
		Group secondGroup = groupService.getOrCreateRegisteredUsersGroup();

		assertNotNull(registeredUsersGroup);
		assertNotNull(secondGroup);
		assertEquals(registeredUsersGroup, secondGroup);
	}
}
