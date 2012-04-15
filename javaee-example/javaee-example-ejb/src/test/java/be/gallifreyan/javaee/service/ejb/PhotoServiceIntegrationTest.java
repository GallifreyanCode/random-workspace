package be.gallifreyan.javaee.service.ejb;

import static be.gallifreyan.javaee.service.ejb.AlbumServiceIntegrationTest.*;
import static be.gallifreyan.javaee.service.ejb.UserServiceIntegrationTest.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

import java.sql.*;
import java.util.List;

import org.junit.*;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.*;

import com.sun.appserv.security.ProgrammaticLogin;

public class PhotoServiceIntegrationTest extends AbstractIntegrationTest
{
	private static final Logger logger = LoggerFactory.getLogger(PhotoServiceIntegrationTest.class);

	static final String TEST_FILE_NAME = "Photo-1.ext";

	static final byte[] TEST_FILE_CONTENT = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	static final String TEST_PHOTO_TITLE = "Photo #1";

	static final String TEST_PHOTO_DESCRIPTION = "My first photo";

	private PhotoService photoService;

	private AlbumService albumService;

	private UserService userService;

	private User user;

	private Album album;

	@BeforeClass
	public static void beforeClass() throws Exception
	{
		logger.info("Entering beforeClass method of {}", PhotoServiceIntegrationTest.class);
		AbstractIntegrationTest.setUpBeforeClass();
	}

	@AfterClass
	public static void afterClass() throws Exception
	{
		logger.info("Entering afterClass method of {}", PhotoServiceIntegrationTest.class);
		AbstractIntegrationTest.tearDownAfterClass();
	}

	@Override
	public void setUp() throws Exception
	{
		logger.info("Entering setUp of method {}", testMethod.getMethodName());
		super.setUp();
		userService = (UserService) context.lookup("java:global/javaee-example/javaee-example-ejb/UserService");
		albumService = (AlbumService) context.lookup("java:global/javaee-example/javaee-example-ejb/AlbumService");
		photoService = (PhotoService) context.lookup("java:global/javaee-example/javaee-example-ejb/PhotoService");

		user = new User(TEST_USER_ID, TEST_PASSWORD);
		userService.signupUser(user);

		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(TEST_USER_ID, TEST_PASSWORD, "JavaEERealm", true);
		album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		albumService.createAlbum(album);
	}

	@Override
	public void tearDown() throws Exception
	{
		logger.info("Entering tearDown of method {}", testMethod.getMethodName());
		super.tearDown();
	}

	@Test
	public void testCreatePhoto() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);

		assertNotNull(uploadedPhoto);
		assertEquals(photo, uploadedPhoto);
		assertEquals(album, uploadedPhoto.getAlbum());

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testCreatePhotoNullValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(null, null);
		photoService.uploadPhoto(photo, album);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testCreatePhotoEmptyValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo("", new byte[] {});
		photoService.uploadPhoto(photo, album);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testCreateDuplicatePhoto() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		photoService.uploadPhoto(photo, album);
		photoService.uploadPhoto(photo, album);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testCreatePhotoInUnownedAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		String anotherUserId = "User#2";
		char[] anotherUserPassword = "PASSWORD".toCharArray();
		User anotherUser = new User(anotherUserId, anotherUserPassword);
		userService.signupUser(anotherUser);
		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(anotherUserId, anotherUserPassword, "JavaEERealm", true);

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		photoService.uploadPhoto(photo, album);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testModifyPhoto() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);

		uploadedPhoto.setTitle(TEST_PHOTO_TITLE);
		uploadedPhoto.setDescription(TEST_PHOTO_DESCRIPTION);
		Photo modifiedPhoto = photoService.modifyPhoto(uploadedPhoto);

		assertNotNull(modifiedPhoto);
		assertEquals(uploadedPhoto, modifiedPhoto);
		assertEquals(album, modifiedPhoto.getAlbum());

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testModifyPhotoNullValues() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);

		uploadedPhoto.setFileName(null);
		uploadedPhoto.setFile(null);
		photoService.modifyPhoto(uploadedPhoto);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testModifyUnownedPhoto() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);

		String anotherUserId = "User#2";
		char[] anotherUserPassword = "PASSWORD".toCharArray();
		User anotherUser = new User(anotherUserId, anotherUserPassword);
		userService.signupUser(anotherUser);
		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(anotherUserId, anotherUserPassword, "JavaEERealm", true);

		uploadedPhoto.setTitle(TEST_PHOTO_TITLE);
		uploadedPhoto.setDescription(TEST_PHOTO_DESCRIPTION);
		photoService.modifyPhoto(uploadedPhoto);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testDeletePhoto() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);
		long photoId = uploadedPhoto.getPhotoId();

		photoService.deletePhoto(uploadedPhoto);

		Connection connection = datasource.getConnection();
		PreparedStatement pStmt = connection.prepareStatement("SELECT COUNT(1) FROM PHOTOS WHERE PHOTOID= ?");
		pStmt.setLong(1, photoId);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next())
		{
			int photoCount = rs.getInt(1);
			assertEquals(0, photoCount);
		}
		else
		{
			fail("The query did not execute successfully.");
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testDeleteUnownedPhoto() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);
		long photoId = uploadedPhoto.getPhotoId();

		String anotherUserId = "User#2";
		char[] anotherUserPassword = "PASSWORD".toCharArray();
		User anotherUser = new User(anotherUserId, anotherUserPassword);
		userService.signupUser(anotherUser);
		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(anotherUserId, anotherUserPassword, "JavaEERealm", true);

		boolean isExceptionThrown = false;
		try
		{
			photoService.deletePhoto(uploadedPhoto);
		}
		catch (PhotoException photoEx)
		{
			isExceptionThrown = true;
		}
		finally
		{
			if (!isExceptionThrown)
			{
				fail("No exception was encountered during the deletion of the photo.");
			}
		}

		Connection connection = datasource.getConnection();
		PreparedStatement pStmt = connection.prepareStatement("SELECT COUNT(1) FROM PHOTOS WHERE PHOTOID= ?");
		pStmt.setLong(1, photoId);
		ResultSet rs = pStmt.executeQuery();
		if (rs.next())
		{
			int photoCount = rs.getInt(1);
			assertEquals(1, photoCount);
		}
		else
		{
			fail("The query did not execute successfully.");
		}
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testFindPhotoByIdWithoutContent() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);
		long photoId = uploadedPhoto.getPhotoId();

		Photo foundPhoto = photoService.findPhotoById(photoId, false);

		assertNotNull(foundPhoto);
		assertEquals(uploadedPhoto.getFileName(), foundPhoto.getFileName());

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testFindPhotoByIdWithContent() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);
		long photoId = uploadedPhoto.getPhotoId();

		Photo foundPhoto = photoService.findPhotoById(photoId, true);

		assertNotNull(foundPhoto);
		assertEquals(uploadedPhoto, foundPhoto);
		assertArrayEquals(TEST_FILE_CONTENT, foundPhoto.getFile());

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testFindUnownedPhotoById() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);
		long photoId = uploadedPhoto.getPhotoId();

		String anotherUserId = "User#2";
		char[] anotherUserPassword = "PASSWORD".toCharArray();
		User anotherUser = new User(anotherUserId, anotherUserPassword);
		userService.signupUser(anotherUser);
		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(anotherUserId, anotherUserPassword, "JavaEERealm", true);

		photoService.findPhotoById(photoId, true);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testFindPhotosByAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		Photo uploadedPhoto = photoService.uploadPhoto(photo, album);

		List<Photo> foundPhotos = photoService.findPhotosByAlbum(album);

		assertNotNull(foundPhotos);
		assertTrue(foundPhotos.contains(uploadedPhoto));

		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testFindPhotosForInvalidAlbum() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		photoService.uploadPhoto(photo, album);

		Album invalidAlbum = new Album(TEST_ALBUM_NAME, TEST_PHOTO_DESCRIPTION);
		invalidAlbum.setAlbumId(-1);
		photoService.findPhotosByAlbum(invalidAlbum);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test
	public void testSetAlbumCover() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		photo = photoService.uploadPhoto(photo, album);
		Photo secondPhoto = new Photo("SecondPhoto", TEST_FILE_CONTENT);
		secondPhoto = photoService.uploadPhoto(secondPhoto, album);

		photoService.setAlbumCover(secondPhoto);

		Album foundAlbum = albumService.findAlbumById(album.getAlbumId());
		assertThat(foundAlbum.getCoverPhoto(), equalTo(secondPhoto));
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testUnownedPhotoAsAlbumCover() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		photo = photoService.uploadPhoto(photo, album);
		Photo secondPhoto = new Photo("SecondFile", TEST_FILE_CONTENT);
		secondPhoto = photoService.uploadPhoto(secondPhoto, album);

		String anotherUserId = "User#2";
		char[] anotherUserPassword = "PASSWORD".toCharArray();
		User anotherUser = new User(anotherUserId, anotherUserPassword);
		userService.signupUser(anotherUser);
		ProgrammaticLogin login = new ProgrammaticLogin();
		login.login(anotherUserId, anotherUserPassword, "JavaEERealm", true);

		photoService.setAlbumCover(secondPhoto);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

	@Test(expected = PhotoException.class)
	public void testPhotoFromAnotherAlbumAsCover() throws Exception
	{
		logger.info("About to execute test method {}", testMethod.getMethodName());

		Photo photo = new Photo(TEST_FILE_NAME, TEST_FILE_CONTENT);
		photo = photoService.uploadPhoto(photo, album);
		Photo secondPhoto = new Photo("SecondFile", TEST_FILE_CONTENT);
		secondPhoto = photoService.uploadPhoto(secondPhoto, album);

		Album secondAlbum = new Album("SecondAlbum", TEST_ALBUM_DESCRIPTION);
		albumService.createAlbum(secondAlbum);
		secondPhoto.modifyAlbum(secondAlbum);
		photoService.setAlbumCover(secondPhoto);

		fail("The execution control flow must not arrive here.");
		logger.info("Finished executing test method {}", testMethod.getMethodName());
	}

}
