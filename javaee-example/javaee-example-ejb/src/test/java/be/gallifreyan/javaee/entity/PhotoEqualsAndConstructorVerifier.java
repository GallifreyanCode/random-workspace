package be.gallifreyan.javaee.entity;

import static org.junit.Assert.*;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class PhotoEqualsAndConstructorVerifier
{

	private static final String TEST_FILENAME = "TESTFILE.TST";
	private static final byte[] TEST_FILECONTENT = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private static final String TEST_FILE_TITLE = "TEST TITLE";
	private static final String TEST_FILE_DESCRIPTION = "TEST_DESCRIPTION";

	/**
	 * Test method for {@link info.galleria.entities.User#hashCode()}.
	 */
	@Test
	public final void testEqualsAndHashcode() throws Exception
	{
		Album firstAlbum = new Album("#1", "Album #1");
		Album secondAlbum = new Album("#2", "Album #2");
		EqualsVerifier.forClass(Photo.class).withPrefabValues(Album.class, firstAlbum, secondAlbum).verify();
	}

	@Test
	public void testTwoArgConstructor() throws Exception
	{
		Photo photo = new Photo(TEST_FILENAME, TEST_FILECONTENT);

		assertNotNull(photo);
		assertEquals(TEST_FILENAME, photo.getFileName());
		assertEquals(TEST_FILECONTENT, photo.getFile());
	}

	@Test
	public void testFourArgConstructor() throws Exception
	{
		Photo photo = new Photo(TEST_FILENAME, TEST_FILECONTENT, TEST_FILE_TITLE, TEST_FILE_DESCRIPTION);

		assertNotNull(photo);
		assertEquals(TEST_FILENAME, photo.getFileName());
		assertEquals(TEST_FILECONTENT, photo.getFile());
		assertEquals(TEST_FILE_TITLE, photo.getTitle());
		assertEquals(TEST_FILE_DESCRIPTION, photo.getDescription());
	}

}
