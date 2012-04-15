package be.gallifreyan.javaee.repo;

import static be.gallifreyan.javaee.repo.GroupRepositoryTest.TEST_GROUP_ID;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.gallifreyan.javaee.entity.Album;
import be.gallifreyan.javaee.entity.Group;
import be.gallifreyan.javaee.entity.Photo;
import be.gallifreyan.javaee.entity.User;
import be.gallifreyan.javaee.service.GenericRepository;

public class UserRepositoryTest extends AbstractRepositoryTest {
	static final Logger logger = LoggerFactory
			.getLogger(UserRepositoryTest.class);
	static final String TEST_USER_ID = "TEST USER #1";
	static final char[] TEST_PASSWORD = "PASSWORD".toCharArray();
	static final String TEST_ALBUM_NAME = "Album #1";
	static final String TEST_ALBUM_DESCRIPTION = "Album Number One";
	static final String TEST_PHOTO_NAME = "Photo #1";
	static final byte[] TEST_PHOTO_CONTENT = "Photo Content".getBytes();

	GenericRepository<User, String> repository;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		repository = new UserRepository(em);
	}

	@Test
	public void testCreateUser() throws Exception {
		User user = new User(TEST_USER_ID, TEST_PASSWORD);

		// Execute
		User newUser = repository.create(user);

		// Verify
		em.flush();
		em.clear();
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, newUser);
		assertEquals(user, actualUser);
	}

	@Test
	public void testModifyUser() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(user);

		// Execute
		char[] modifiedPassword = "NEWPASSWORD".toCharArray();
		user.setPassword(modifiedPassword);
		User modifiedUser = repository.modify(user);

		// Verify
		em.flush();
		em.clear();
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, modifiedUser);
		assertArrayEquals(modifiedPassword, actualUser.getPassword());
	}

	@Test
	public void testModifyUserAddGroup() throws Exception {
		// Setup
		Group group = new Group(TEST_GROUP_ID);
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(group);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		user.addToGroups(group);
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		assertTrue(actualUser.getGroups().contains(group));
	}

	@Test
	public void testModifyUserRemoveGroup() throws Exception {
		// Setup
		Group group = new Group(TEST_GROUP_ID);
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		user.addToGroups(group);
		em.persist(group);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		user.removeFromGroups(group);
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		Group actualGroup = em.find(Group.class, TEST_GROUP_ID);
		assertEquals(user, actualUser);
		assertFalse(actualUser.getGroups().contains(group));
		assertEquals(group, actualGroup);
	}

	@Test
	public void testModifyUserAddAlbum() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		user.addToAlbums(album);
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		assertTrue(actualUser.getAlbums().contains(album));
	}

	@Test
	public void testModifyUserModifyAlbum() throws Exception {
		// Setup
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		user.addToAlbums(album);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		String modifiedAlbumName = "Modified Album Name";
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.setName(modifiedAlbumName);
			}
		}
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		for (Album actualAlbum : actualUser.getAlbums()) {
			assertFalse(actualAlbum.getName().equals(TEST_ALBUM_NAME));
			assertTrue(actualAlbum.getName().equals(modifiedAlbumName));
		}
	}

	@Test
	public void testModifyUserRemoveAlbum() throws Exception {
		// Setup
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		user.addToAlbums(album);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		user.removeFromAlbums(album);
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		assertFalse(actualUser.getAlbums().contains(album));
	}

	@Test
	public void testModifyUserRemoveAlbumWithPhoto() throws Exception {
		// Setup
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		user.addToAlbums(album);
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.addToPhotos(photo);
			}
		}
		em.persist(user);
		long albumId = album.getAlbumId();
		long photoId = photo.getPhotoId();

		em.flush();
		em.clear();

		// Execute
		user.removeFromAlbums(album);
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		Album actualAlbum = em.find(Album.class, albumId);
		Photo actualPhoto = em.find(Photo.class, photoId);
		assertEquals(user, actualUser);
		assertFalse(actualUser.getAlbums().contains(album));
		assertNull(actualAlbum);
		assertNull(actualPhoto);
	}

	@Test
	public void testModifyUserAddPhoto() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		user.addToAlbums(album);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.addToPhotos(photo);
			}
		}
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				assertTrue(anAlbum.getPhotos().contains(photo));
			}
		}
	}

	@Test
	public void testModifyUserModifyPhoto() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		user.addToAlbums(album);
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.addToPhotos(photo);
			}
		}
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		String modifiedTitle = "Modified Photo Title";
		for (Album anAlbum : user.getAlbums()) {
			for (Photo aPhoto : anAlbum.getPhotos()) {
				aPhoto.setTitle(modifiedTitle);
			}
		}
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		for (Album anAlbum : user.getAlbums()) {
			assertTrue(anAlbum.getPhotos().contains(photo));
			for (Photo aPhoto : anAlbum.getPhotos()) {
				assertEquals(modifiedTitle, aPhoto.getTitle());
			}
		}
	}

	@Test
	public void testModifyUserRemovePhoto() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		user.addToAlbums(album);
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.addToPhotos(photo);
			}
		}
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.removeFromPhotos(photo);
			}
		}
		user = repository.modify(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertEquals(user, actualUser);
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				assertFalse(anAlbum.getPhotos().contains(photo));
			}
		}
	}

	@Test
	public void testDeleteUser() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(user);
		em.flush();
		em.clear();

		// Execute
		repository.delete(user);

		// Verify
		em.flush();
		em.clear();
		User actualUser = em.find(User.class, TEST_USER_ID);
		assertNull(actualUser);
	}

	@Test
	public void testDeleteUserWithAlbumAndPhotos() throws Exception {
		// Setup
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		user.addToAlbums(album);
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		for (Album anAlbum : user.getAlbums()) {
			if (anAlbum.equals(album)) {
				anAlbum.addToPhotos(photo);
			}
		}
		em.persist(user);
		long albumId = album.getAlbumId();
		long photoId = photo.getPhotoId();

		em.flush();
		em.clear();

		// Execute
		repository.delete(user);
		em.flush();
		em.clear();

		// Verify
		User actualUser = em.find(User.class, TEST_USER_ID);
		Album actualAlbum = em.find(Album.class, albumId);
		Photo actualPhoto = em.find(Photo.class, photoId);
		assertNull(actualUser);
		assertNull(actualAlbum);
		assertNull(actualPhoto);
	}

	@Test
	public void testFindUserById() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(user);

		em.flush();
		em.clear();

		// Execute
		User actualUser = repository.findById(TEST_USER_ID);

		// Verify
		assertEquals(user, actualUser);
	}

	@Test
	public void testFindAllUsers() throws Exception {
		// Setup
		User user = new User(TEST_USER_ID, TEST_PASSWORD);
		em.persist(user);

		String secondUserId = "TEST_USER #2";
		char[] secondUserPassword = "NEWPASSWORD".toCharArray();
		User secondUser = new User(secondUserId, secondUserPassword);
		em.persist(secondUser);

		em.flush();
		em.clear();

		// Execute
		List<User> users = repository.findAll();

		// Verify
		assertTrue(users.contains(user));
		assertTrue(users.contains(secondUser));
	}
}
