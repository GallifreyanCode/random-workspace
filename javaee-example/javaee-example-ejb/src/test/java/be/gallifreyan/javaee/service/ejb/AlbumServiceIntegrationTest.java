package be.gallifreyan.javaee.service.ejb;

import static be.gallifreyan.javaee.service.ejb.UserServiceIntegrationTest.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.List;

import org.junit.*;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.*;

import com.sun.appserv.security.ProgrammaticLogin;

public class AlbumServiceIntegrationTest extends AbstractIntegrationTest
{
	private static final Logger logger = LoggerFactory.getLogger(AlbumServiceIntegrationTest.class);

	static final String TEST_ALBUM_NAME = "Album #1";

	static final String TEST_ALBUM_DESCRIPTION = "My first album";

	private AlbumService albumService;

	private UserService userService;

	private User user;

	@BeforeClass
	public static void beforeClass() throws Exception
	{
		logger.info("Entering beforeClass method of {}", AlbumServiceIntegrationTest.class);
		AbstractIntegrationTest.setUpBeforeClass();
	}

	@AfterClass
	public static void afterClass() throws Exception
	{
		logger.info("Entering afterClass method of {}", AlbumServiceIntegrationTest.class);
		AbstractIntegrationTest.tearDownAfterClass();
	}

	@Override
	public void setUp() throws Exception
	{
		logger.info("Entering setUp of method {}", testMethod.getMethodName());
		super.setUp();
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");
		albumService = (AlbumService) context.lookup("java:global/javaee-example/javaee-example-ejb/AlbumService");

		user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);
	}

	@Override
	public void tearDown() throws Exception
	{
		logger.info("Entering tearDown of method {}", testMethod.getMethodName());
		super.tearDown();
	}

	@Test
	public void testCreateNewAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);

		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		assertEquals(album, createdAlbum);
		assertEquals(user, album.getUser());
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testCreateDuplicateAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		albumService.createAlbum(album);

		Album duplicateAlbum = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		albumService.createAlbum(duplicateAlbum);
		fail("The execution control flow must not arrive here.");

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testCreateAlbumNullValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);

		Album album = new Album(null, null);
		albumService.createAlbum(album);
		fail("The execution control flow must not arrive here.");

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testCreateAlbumEmptyValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);

		Album album = new Album("", "");
		albumService.createAlbum(album);
		fail("The execution control flow must not arrive here.");

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testModifyAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		String newName = "New Album name";
		String newDescription = "New Album Description";
		createdAlbum.setName(newName);
		createdAlbum.setDescription(newDescription);
		Album modifiedAlbum = albumService.modifyAlbum(createdAlbum);

		assertEquals(createdAlbum, modifiedAlbum);
		assertEquals(user, modifiedAlbum.getUser());
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testModifyAlbumAmongMultiple() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);
		Album secondAlbum = new Album("AnotherAlbum", TEST_ALBUM_DESCRIPTION);
		Album anotherAlbum = albumService.createAlbum(secondAlbum);

		String newName = "New Album name";
		String newDescription = "New Album Description";
		createdAlbum.setName(newName);
		createdAlbum.setDescription(newDescription);
		Album modifiedAlbum = albumService.modifyAlbum(createdAlbum);

		assertEquals(createdAlbum, modifiedAlbum);
		assertEquals(user, modifiedAlbum.getUser());
		assertThat(anotherAlbum, not(equalTo(modifiedAlbum)));
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testModifyAlbumIntoDuplicate() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		albumService.modifyAlbum(createdAlbum);
		fail("The execution control flow must not arrive here.");

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testModifyUnownedAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		String newUserId = "User#2";
		User newUser = new User(newUserId, TEST_PASSWORD);
		userService.signupUser(newUser);
		login.login(newUserId, TEST_PASSWORD, "JavaEERealm", true);

		albumService.modifyAlbum(createdAlbum);
		fail("The execution control flow must not arrive here.");

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testDeleteAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);
		long albumId = createdAlbum.getAlbumId();

		albumService.deleteAlbum(createdAlbum);

		Connection connection = datasource.getConnection();
		PreparedStatement pStmt = connection.prepareStatement("SELECT COUNT(1) FROM ALBUMS WHERE ALBUMID=?");
		pStmt.setLong(1, albumId);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next())
		{
			int albumCount = rs.getInt(1);
			assertEquals(0, albumCount);
		}
		else
		{
			fail("The query did not execute successfully.");
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testDeleteAlbumAmongMultiple() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);
		Album secondAlbum = new Album("AnotherAlbum", TEST_ALBUM_DESCRIPTION);
		albumService.createAlbum(secondAlbum);
		long albumId = createdAlbum.getAlbumId();

		albumService.deleteAlbum(createdAlbum);

		Connection connection = datasource.getConnection();
		PreparedStatement findAllAlbumsStmt = connection.prepareStatement("SELECT COUNT(1) FROM ALBUMS");
		ResultSet allAlbumsRSet = findAllAlbumsStmt.executeQuery();
		if (allAlbumsRSet.next())
		{
			int albumCount = allAlbumsRSet.getInt(1);
			assertEquals(1, albumCount);
		}
		else
		{
			fail("The query did not execute successfully.");
		}
		PreparedStatement findDeletedAlbumStmt = connection
				.prepareStatement("SELECT COUNT(1) FROM ALBUMS WHERE ALBUMID=?");
		findDeletedAlbumStmt.setLong(1, albumId);
		ResultSet deletedAlbumsRSet = findDeletedAlbumStmt.executeQuery();
		if (deletedAlbumsRSet.next())
		{
			int albumCount = deletedAlbumsRSet.getInt(1);
			assertEquals(0, albumCount);
		}
		else
		{
			fail("The query did not execute successfully.");
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testDeleteUnownedAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		String newUserId = "User#2";
		User newUser = new User(newUserId, TEST_PASSWORD);
		userService.signupUser(newUser);
		login.login(newUserId, TEST_PASSWORD, "JavaEERealm", true);

		albumService.deleteAlbum(createdAlbum);
		fail("The execution control flow must not arrive here.");
	}

	@Test
	public void testFindCurrentUserAlbums() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		List<Album> albums = albumService.findCurrentUserAlbums();

		assertNotNull(albums);
		assertEquals(1, albums.size());
		assertEquals(createdAlbum, albums.get(0));
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testFindAlbumsByOwner() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		List<Album> albums = albumService.findAllAlbumByOwner(user);

		assertNotNull(albums);
		assertEquals(1, albums.size());
		assertEquals(createdAlbum, albums.get(0));
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testFindAlbumsForInvalidOwner() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		String invalidUserId = "User#2";
		User invalidUser = new User(invalidUserId, TEST_PASSWORD);

		List<Album> albums = albumService.findAllAlbumByOwner(invalidUser);

		assertNotNull(albums);
		assertEquals(1, albums.size());
		assertEquals(createdAlbum, albums.get(0));
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testFindAlbumById() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Album createdAlbum = albumService.createAlbum(album);

		long albumId = createdAlbum.getAlbumId();
		Album foundAlbum = albumService.findAlbumById(albumId);

		assertNotNull(foundAlbum);
		assertEquals(createdAlbum, foundAlbum);
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = AlbumException.class)
	public void testFindNonexistentAlbumById() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		albumService.createAlbum(album);

		long albumId = -1;
		albumService.findAlbumById(albumId);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}
}
