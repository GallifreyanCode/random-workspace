package be.gallifreyan.javaee.repo;

import static be.gallifreyan.javaee.repo.UserRepositoryTest.*;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;
import org.slf4j.*;

import be.gallifreyan.javaee.entity.*;

public class PhotoRepositoryTest extends AbstractRepositoryTest
{
	static final Logger logger = LoggerFactory.getLogger(AlbumRepositoryTest.class);

	private PhotoRepository repository;
	private AlbumRepository albumRepository;
	private UserRepository userRepository;

	private User user;

	private Album album;

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		user = new User(TEST_USER_ID, TEST_PASSWORD);
		userRepository = new UserRepository(em);
		userRepository.create(user);
		em.flush();

		album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		album.modifyUser(user);
		albumRepository = new AlbumRepository(em);
		albumRepository.create(album);
		em.flush();

		repository = new PhotoRepository(em);
	}

	@Test
	public void testCreatePhoto() throws Exception
	{
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.modifyAlbum(album);
		photo.setUploadTime(new Date());

		// Execute
		Photo createdPhoto = repository.create(photo);
		long photoId = createdPhoto.getPhotoId();
		em.flush();

		// Verify
		em.clear();
		Photo foundPhoto = em.find(Photo.class, photoId);
		assertNotNull(foundPhoto);
		assertEquals(createdPhoto, foundPhoto);
	}

	@Test
	public void testModifyPhoto() throws Exception
	{
		// Setup
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.modifyAlbum(album);
		photo.setUploadTime(new Date());

		Photo createdPhoto = repository.create(photo);
		long photoId = createdPhoto.getPhotoId();
		em.flush();
		em.clear();

		// Execute
		String modifiedTitle = "New Title";
		String modifiedDescription = "New Description";
		createdPhoto.setTitle(modifiedTitle);
		createdPhoto.setDescription(modifiedDescription);
		Photo modifiedPhoto = repository.modify(createdPhoto);
		em.flush();
		em.clear();

		// Verify
		Photo foundPhoto = em.find(Photo.class, photoId);
		assertNotNull(foundPhoto);
		assertEquals(modifiedPhoto, foundPhoto);
		assertEquals(modifiedTitle, foundPhoto.getTitle());
		assertEquals(modifiedDescription, foundPhoto.getDescription());
	}

	@Test
	public void testDeletePhoto() throws Exception
	{
		// Setup
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.modifyAlbum(album);
		photo.setUploadTime(new Date());

		Photo createdPhoto = repository.create(photo);
		long photoId = createdPhoto.getPhotoId();
		em.flush();
		em.clear();

		// Execute
		repository.delete(createdPhoto);

		// Verify
		em.flush();
		em.clear();
		Photo foundPhoto = em.find(Photo.class, photoId);
		assertNull(foundPhoto);
	}

	@Test
	public void testFindPhotoById() throws Exception
	{
		// Setup
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.modifyAlbum(album);
		photo.setUploadTime(new Date());

		Photo createdPhoto = repository.create(photo);
		long photoId = createdPhoto.getPhotoId();
		em.flush();
		em.clear();

		// Execute
		Photo foundPhoto = repository.findById(photoId);

		// Verify
		assertNotNull(foundPhoto);
		assertEquals(createdPhoto, foundPhoto);
	}

	@Test
	public void testFindAllPhotos() throws Exception
	{
		// Setup
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.modifyAlbum(album);
		photo.setUploadTime(new Date());

		Photo createdPhoto = repository.create(photo);
		em.flush();
		em.clear();

		// Execute
		List<Photo> foundPhotos = repository.findAll();

		// Verify
		assertNotNull(foundPhotos);
		assertTrue(foundPhotos.contains(createdPhoto));
	}

	@Test
	public void testFindAllPhotosByAlbum() throws Exception
	{
		// Setup
		Photo photo = new Photo(TEST_PHOTO_NAME, TEST_PHOTO_CONTENT);
		photo.modifyAlbum(album);
		photo.setUploadTime(new Date());

		Photo createdPhoto = repository.create(photo);
		em.flush();
		em.clear();

		// Execute
		List<Photo> foundPhotos = repository.findPhotosByAlbum(album.getAlbumId());

		// Verify
		assertNotNull(foundPhotos);
		assertTrue(foundPhotos.contains(createdPhoto));
	}
}
