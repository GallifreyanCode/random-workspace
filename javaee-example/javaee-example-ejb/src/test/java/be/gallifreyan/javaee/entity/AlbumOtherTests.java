package be.gallifreyan.javaee.entity;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class AlbumOtherTests
{

	private static final String TEST_ALBUM_NAME = "TEST ALBUM";
	private static final String TEST_ALBUM_DESCRIPTION = "TEST ALBUM DESCRIPTION";

	/**
	 * Test method for {@link info.galleria.entities.User#hashCode()}.
	 */
	@Test
	public final void testEqualsAndHashcode() throws Exception
	{
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		Photo secondPhoto = new Photo("#2", new byte[] { 2 }, "#2", "#2");
		EqualsVerifier.forClass(Album.class).withPrefabValues(Photo.class, firstPhoto, secondPhoto).verify();
	}

	@Test
	public void testNoArgConstructor() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);

		assertNotNull(album);
		assertEquals(TEST_ALBUM_NAME, album.getName());
		assertEquals(TEST_ALBUM_DESCRIPTION, album.getDescription());
	}

	@Test
	public void testSetCoverOnAddition() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");

		album.addToPhotos(firstPhoto);

		assertEquals(1, album.getPhotos().size());
		assertTrue(album.getPhotos().contains(firstPhoto));
		assertEquals(firstPhoto, album.getCoverPhoto());
	}

	@Test
	public void testSetCoverOnMultipleAddition() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		Photo secondPhoto = new Photo("#2", new byte[] { 2 }, "#2", "#2");

		album.addToPhotos(firstPhoto);
		album.addToPhotos(secondPhoto);

		assertEquals(2, album.getPhotos().size());
		assertThat(album.getPhotos(), hasItems(firstPhoto, secondPhoto));
		assertEquals(firstPhoto, album.getCoverPhoto());
	}

	@Test
	public void testClearCoverPhoto() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		album.addToPhotos(firstPhoto);

		album.clearCoverPhoto();

		assertEquals(1, album.getPhotos().size());
		assertTrue(album.getPhotos().contains(firstPhoto));
		assertEquals(null, album.getCoverPhoto());
	}

	@Test
	public void testClearCoverForEmptyAlbum() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);

		album.clearCoverPhoto();

		assertEquals(null, album.getCoverPhoto());
	}

	@Test
	public void testModifyCoverPhotoWithNull() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		album.addToPhotos(firstPhoto);

		album.modifyCoverPhoto(null);

		assertEquals(1, album.getPhotos().size());
		assertTrue(album.getPhotos().contains(firstPhoto));
		assertEquals(firstPhoto, album.getCoverPhoto());
	}

	@Test
	public void testModifyCoverPhotoWithValidPhoto() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		Photo secondPhoto = new Photo("#2", new byte[] { 2 }, "#2", "#2");
		album.addToPhotos(firstPhoto);
		album.addToPhotos(secondPhoto);

		album.modifyCoverPhoto(secondPhoto);

		assertEquals(2, album.getPhotos().size());
		assertTrue(album.getPhotos().contains(firstPhoto));
		assertTrue(album.getPhotos().contains(secondPhoto));
		assertEquals(secondPhoto, album.getCoverPhoto());
	}

	@Test
	public void testModifyAlbumCoverToExisting() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		album.addToPhotos(firstPhoto);

		album.modifyCoverPhoto(firstPhoto);

		assertEquals(1, album.getPhotos().size());
		assertEquals(firstPhoto, album.getCoverPhoto());
	}

	@Test
	public void testGetCoverPhotoForEmptyAlbum() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		Photo secondPhoto = new Photo("#2", new byte[] { 2 }, "#2", "#2");
		album.addToPhotos(firstPhoto);
		album.addToPhotos(secondPhoto);

		album.removeFromPhotos(firstPhoto);
		album.removeFromPhotos(secondPhoto);

		assertEquals(0, album.getPhotos().size());
		assertEquals(null, album.getCoverPhoto());
	}

	@Test
	public void testGetCoverPhotoOnRemove() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		Photo secondPhoto = new Photo("#2", new byte[] { 2 }, "#2", "#2");
		album.addToPhotos(firstPhoto);
		album.addToPhotos(secondPhoto);

		album.removeFromPhotos(firstPhoto);

		assertEquals(1, album.getPhotos().size());
		assertTrue(album.getPhotos().contains(secondPhoto));
		assertEquals(secondPhoto, album.getCoverPhoto());
	}

	@Test
	public void testRandomCoverPhotoOnRemove() throws Exception
	{
		Album album = new Album(TEST_ALBUM_NAME, TEST_ALBUM_DESCRIPTION);
		Photo firstPhoto = new Photo("#1", new byte[] { 1 }, "#1", "#1");
		Photo secondPhoto = new Photo("#2", new byte[] { 2 }, "#2", "#2");
		Photo thirdPhoto = new Photo("#3", new byte[] { 3 }, "#3", "#3");
		Photo fourthPhoto = new Photo("#4", new byte[] { 4 }, "#4", "#4");
		Photo fifthPhoto = new Photo("#5", new byte[] { 5 }, "#5", "#5");
		album.addToPhotos(firstPhoto);
		album.addToPhotos(secondPhoto);
		album.addToPhotos(thirdPhoto);
		album.addToPhotos(fourthPhoto);
		album.addToPhotos(fifthPhoto);

		album.removeFromPhotos(firstPhoto);

		assertEquals(4, album.getPhotos().size());
		assertThat(album.getPhotos(), hasItems(secondPhoto, thirdPhoto, fourthPhoto, fifthPhoto));
		assertThat(album.getPhotos(), hasItem(album.getCoverPhoto()));
	}

}
