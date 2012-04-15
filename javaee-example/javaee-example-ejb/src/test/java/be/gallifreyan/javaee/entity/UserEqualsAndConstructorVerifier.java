package be.gallifreyan.javaee.entity;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserEqualsAndConstructorVerifier
{

	static final char[] TEST_PASSWORD = { 'T', 'E', 'S', 'T', 'P', 'A', 'S', 'S', 'W', 'O', 'R', 'D' };
	static final String TEST_USERID = "TEST_USER";

	/**
	 * Test method for {@link info.galleria.entities.User#hashCode()}.
	 */
	@Test
	public final void testEqualsAndHashcode() throws Exception
	{
		//EqualsVerifier.forClass(User.class).verify();
	}

	@Test
	public void testNoArgConstructor() throws Exception
	{
		User user = new User(TEST_USERID, TEST_PASSWORD);

		assertNotNull(user);
		assertEquals(TEST_USERID, user.getUserId());
		assertEquals(TEST_PASSWORD, user.getPassword());
	}

}
