package be.gallifreyan.javaee.repo;

import static be.gallifreyan.javaee.repo.UserRepositoryTest.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.util.*;

import org.junit.Test;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.*;

public class AlbumRepositoryTest extends AbstractRepositoryTest
{
	static final Logger logger = LoggerFactory.getLogger(AlbumRepositoryTest.class);

	private AlbumRepository repository;
	private UserRepository userRepository;

	private User user;

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		userRepository = new UserRepository(em);
		user = new User(TEST_USER_ID, TEST_PASSWORD);
		userRepository.create(user);
		em.flush();

		repository = new AlbumRepository(em);
	}

	@Test
	public void testCreateNewAlbum() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);

		// Execute
		Album createdAlbum = repository.create(album);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, createdAlbum.getAlbumId());
		assertEquals(album, actualAlbum);
		assertNull(actualAlbum.getCoverPhoto());
	}

	@Test
	public void testModifyAlbum() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		// Execute
		String modifiedName = "Modified Name";
		String modifiedDescription = "Modified Description";
		createdAlbum.setName(modifiedName);
		createdAlbum.setDescription(modifiedDescription);

		Album modifiedAlbum = repository.modify(createdAlbum);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, modifiedAlbum.getAlbumId());
		assertEquals(modifiedAlbum, actualAlbum);
		assertEquals(modifiedName, actualAlbum.getName());
		assertEquals(modifiedDescription, actualAlbum.getDescription());
		assertNull(actualAlbum.getCoverPhoto());
	}

	@Test
	public void testModifyAlbumAddPhoto() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		// Execute
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		createdAlbum.addToPhotos(photo);
		Album modifiedAlbum = repository.modify(createdAlbum);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, modifiedAlbum.getAlbumId());
		assertEquals(modifiedAlbum, actualAlbum);
		assertTrue(actualAlbum.getPhotos().contains(photo));
		assertEquals(photo, actualAlbum.getCoverPhoto());
	}

	@Test
	public void testModifyAlbumAddMulitplePhotos() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		// Execute
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		createdAlbum.addToPhotos(photo);
		Photo secondPhoto = new Photo("Photo #2", "Photo Content #2".getBytes());
		secondPhoto.setUploadTime(new Date());
		createdAlbum.addToPhotos(secondPhoto);
		Album modifiedAlbum = repository.modify(createdAlbum);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, modifiedAlbum.getAlbumId());
		assertEquals(modifiedAlbum, actualAlbum);
		assertThat(actualAlbum.getPhotos(), hasItems(photo, secondPhoto));
		assertEquals(photo, actualAlbum.getCoverPhoto());
	}

	@Test
	public void testModifyAlbumRemovePhoto() throws Exception
	{
		// Setup
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		createdAlbum.addToPhotos(photo);
		createdAlbum = repository.modify(createdAlbum);
		em.flush();

		// Execute
		createdAlbum.removeFromPhotos(photo);
		Album modifiedAlbum = repository.modify(createdAlbum);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, modifiedAlbum.getAlbumId());
		assertEquals(modifiedAlbum, actualAlbum);
		assertFalse(actualAlbum.getPhotos().contains(photo));
		assertNull(actualAlbum.getCoverPhoto());
	}

	@Test
	public void testModifyAlbumRemoveMultiplePhotos() throws Exception
	{
		// Setup
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		createdAlbum.addToPhotos(photo);
		Photo secondPhoto = new Photo("Photo #2", "Photo Content #2".getBytes());
		secondPhoto.setUploadTime(new Date());
		createdAlbum.addToPhotos(secondPhoto);
		Photo thirdPhoto = new Photo("Photo #3", "Photo Content #3".getBytes());
		thirdPhoto.setUploadTime(new Date());
		createdAlbum.addToPhotos(thirdPhoto);
		createdAlbum = repository.modify(createdAlbum);
		em.flush();

		// Execute
		createdAlbum.removeFromPhotos(photo);
		Album modifiedAlbum = repository.modify(createdAlbum);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, modifiedAlbum.getAlbumId());
		assertEquals(modifiedAlbum, actualAlbum);
		assertThat(actualAlbum.getPhotos(), hasItems(secondPhoto, thirdPhoto));
		assertThat(actualAlbum.getCoverPhoto(), either(equalTo(secondPhoto)).or(equalTo(thirdPhoto)));
	}

	@Test
	public void testModifyAlbumModifyPhoto() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		createdAlbum.addToPhotos(photo);
		createdAlbum = repository.modify(createdAlbum);
		em.flush();

		// Execute
		String modifiedTitle = "New Title";
		String modifiedDescription = "New Description";

		long photoId = 0;
		for (Photo eachPhoto : createdAlbum.getPhotos())
		{
			if (eachPhoto.equals(photo))
			{
				photoId = eachPhoto.getPhotoId();
				eachPhoto.setTitle(modifiedTitle);
				eachPhoto.setDescription(modifiedDescription);
				break;
			}
		}
		Album modifiedAlbum = repository.modify(createdAlbum);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, modifiedAlbum.getAlbumId());
		Photo actualPhoto = em.find(Photo.class, photoId);
		assertEquals(modifiedAlbum, actualAlbum);
		assertTrue(actualAlbum.getPhotos().contains(photo));
		assertEquals(modifiedTitle, actualPhoto.getTitle());
		assertEquals(modifiedDescription, actualPhoto.getDescription());
		assertEquals(actualAlbum.getCoverPhoto(), actualPhoto);
		assertEquals(photo, actualAlbum.getCoverPhoto());
	}

	@Test
	public void testDeleteAlbum() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		em.persist(album);
		long albumId = album.getAlbumId();
		em.flush();

		// Execute
		repository.delete(album);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, albumId);
		assertNull(actualAlbum);
	}

	@Test
	public void testDeleteAlbumWithPhoto() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		em.persist(album);
		em.flush();

		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.setUploadTime(new Date());
		album.addToPhotos(photo);
		album = repository.modify(album);
		em.flush();

		// Execute
		long albumId = album.getAlbumId();
		repository.delete(album);

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, albumId);
		assertNull(actualAlbum);
	}

	@Test
	public void testFindAlbumById() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		// Execute
		Album foundAlbum = repository.findById(createdAlbum.getAlbumId());

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, foundAlbum.getAlbumId());
		assertEquals(foundAlbum, actualAlbum);
	}

	@Test
	public void testFindAllAlbums() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		// Execute
		List<Album> foundAlbums = repository.findAll();

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, createdAlbum.getAlbumId());
		assertEquals(1, foundAlbums.size());
		assertTrue(foundAlbums.contains(actualAlbum));
	}

	@Test
	public void testFindAllAlbumsByOwner() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		Album createdAlbum = repository.create(album);

		// Execute
		List<Album> foundAlbums = repository.findAllByOwner(user.getUserId());

		// Verify
		em.flush();
		em.clear();
		Album actualAlbum = em.find(Album.class, createdAlbum.getAlbumId());
		assertEquals(1, foundAlbums.size());
		assertTrue(foundAlbums.contains(actualAlbum));
	}
}
