package be.gallifreyan.javaee.entity;

import static org.junit.Assert.*;

import nl.jqno.equalsverifier.EqualsVerifier;

import org.junit.Test;

public class GroupEqualsAndConstructorVerifier
{

	private static final String TEST_GROUPID = "TEST_GROUP";

	/**
	 * Test method for {@link info.galleria.entities.User#hashCode()}.
	 */
	@Test
	public final void testEqualsAndHashcode() throws Exception
	{
		EqualsVerifier.forClass(Group.class).verify();
	}

	@Test
	public void testNoArgConstructor() throws Exception
	{
		Group group = new Group(TEST_GROUPID);

		assertNotNull(group);
		assertEquals(TEST_GROUPID, group.getGroupId());
	}

}
