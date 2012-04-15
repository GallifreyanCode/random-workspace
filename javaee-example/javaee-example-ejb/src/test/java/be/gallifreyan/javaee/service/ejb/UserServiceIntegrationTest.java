package be.gallifreyan.javaee.service.ejb;

import static org.junit.Assert.*;

import java.sql.*;
import java.util.Arrays;

import org.junit.*;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.User;
import be.gallifreyan.javaee.util.PasswordUtility;

import com.sun.appserv.security.ProgrammaticLogin;

public class UserServiceIntegrationTest extends AbstractIntegrationTest
{
	private static final Logger logger = LoggerFactory.getLogger(GroupServiceIntegrationTest.class);

	static final String TEST_USER_ID = "User #1";

	static final char[] TEST_PASSWORD = "password".toCharArray();

	private UserService userService;

	@BeforeClass
	public static void beforeClass() throws Exception
	{
		logger.info("Entering beforeClass method of {}", UserServiceIntegrationTest.class);
		AbstractIntegrationTest.setUpBeforeClass();
	}

	@AfterClass
	public static void afterClass() throws Exception
	{
		logger.info("Entering afterClass method of {}", UserServiceIntegrationTest.class);
		AbstractIntegrationTest.tearDownAfterClass();
	}

	@Override
	public void setUp() throws Exception
	{
		logger.info("Entering setUp of method {}", testMethod.getMethodName());
		super.setUp();
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");
	}

	@Override
	public void tearDown() throws Exception
	{
		logger.info("Entering tearDown of method {}", testMethod.getMethodName());
		super.tearDown();
	}

	@Test
	public final void testCreateUserWithNullValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(null, null);
		try
		{
			userService.signupUser(user);
		}
		catch (UserException userEx)
		{
			assertNotNull(userEx.getViolations());
			assertFalse(userEx.getViolations().size() == 0);
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public final void testCreateUserWithEmptyValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		try
		{
			User user = new User("", "".toCharArray());
			userService.signupUser(user);
		}
		catch (UserException userEx)
		{
			assertNotNull(userEx.getViolations());
			assertFalse(userEx.getViolations().size() == 0);
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public final void testCreateUserWithLongValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		char[] userId = new char[1000];
		Arrays.fill(userId, '0');

		try
		{
			User user = new User(new String(userId), TEST_PASSWORD);
			userService.signupUser(user);
		}
		catch (UserException userEx)
		{
			assertNotNull(userEx.getViolations());
			assertFalse(userEx.getViolations().size() == 0);
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public final void testCreateUser() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		User actualUser = userService.signupUser(user);
		assertTrue(actualUser != null);
		assertEquals(TEST_USER_ID, actualUser.getUserId());
		assertFalse(Arrays.equals(TEST_PASSWORD, actualUser.getPassword()));
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = UserException.class)
	public final void testCreateDuplicateUser() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		User actualUser = userService.signupUser(user);
		assertTrue(actualUser != null);

		User duplicateUser = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(duplicateUser);
		fail("The execution control flow must not arrive here.");

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public final void testRemoveUser() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		userService.deleteUserAccount();
		login.logout();

		Connection conn = datasource.getConnection();
		PreparedStatement pStmt = conn.prepareStatement("SELECT COUNT(1) FROM USERS WHERE USERID=?");
		pStmt.setString(1, TEST_USER_ID);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next())
		{
			int rowCount = rs.getInt(1);
			assertEquals(0, rowCount);
		}

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = SecurityException.class)
	public final void testRemoveNonexistentUser() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login("User #2", TEST_PASSWORD, "JavaEERealm", true);
		fail("The execution control flow must not arrive here.");
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		userService.deleteUserAccount();
		login.logout();

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = UserException.class)
	public final void testRemoveDeletedUser() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");
		userService.deleteUserAccount();

		userService.deleteUserAccount();
		login.logout();
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = ModifyPasswordException.class)
	public final void testModifyPasswordToNull() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		ModifyPasswordRequest request = new ModifyPasswordRequest(TEST_PASSWORD, null, null);
		userService.modifyPassword(request);
		fail("The execution control flow must not arrive here.");
		login.logout();

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public final void testModifyPassword() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		char[] password = "password123".toCharArray();
		ModifyPasswordRequest request = new ModifyPasswordRequest(TEST_PASSWORD, password, password);
		userService.modifyPassword(request);
		login.logout();

		char[] expectedDigestInChars = PasswordUtility.getDigest("password123".toCharArray(), "SHA-512");
		String expectedDigest = new String(expectedDigestInChars);
		Connection conn = datasource.getConnection();
		PreparedStatement pStmt = conn.prepareStatement("SELECT password FROM USERS WHERE USERID=?");
		pStmt.setString(1, TEST_USER_ID);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next())
		{
			String digest = rs.getString(1);
			assertEquals(expectedDigest, digest);
		}

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = ModifyPasswordException.class)
	public final void testModifyIncorrectPassword() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		char[] incorrectOriginalPassword = "password1".toCharArray();
		char[] password = "password2".toCharArray();
		ModifyPasswordRequest request = new ModifyPasswordRequest(incorrectOriginalPassword, password, password);
		userService.modifyPassword(request);
		login.logout();

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = ModifyPasswordException.class)
	public final void testModifySamePassword() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		ModifyPasswordRequest request = new ModifyPasswordRequest(TEST_PASSWORD, TEST_PASSWORD, TEST_PASSWORD);
		userService.modifyPassword(request);
		login.logout();

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = ModifyPasswordException.class)
	public final void testModifyPasswordIncorrectConfirmation() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");

		char[] password = "password123".toCharArray();
		char[] confirmPassword = "password1234".toCharArray();
		ModifyPasswordRequest request = new ModifyPasswordRequest(TEST_PASSWORD, password, confirmPassword);
		userService.modifyPassword(request);
		login.logout();

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}
}
