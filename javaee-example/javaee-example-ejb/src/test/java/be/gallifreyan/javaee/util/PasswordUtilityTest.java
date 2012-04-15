package be.gallifreyan.javaee.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordUtilityTest {

	@Test
	public void testCreatePasswordDigestZeroChars() throws Exception {
		char[] password = {};
		char[] digest = PasswordUtility.getDigest(password, "SHA-512");
		char[] expectedDigest = "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e"
				.toCharArray();
		assertArrayEquals(expectedDigest, digest);
	}

	@Test
	public void testCreatePasswordDigestSingleASCIIChar() throws Exception {
		char[] password = { 'a' };
		char[] digest = PasswordUtility.getDigest(password, "SHA-512");
		char[] expectedDigest = "1f40fc92da241694750979ee6cf582f2d5d7d28e18335de05abc54d0560e0f5302860c652bf08d560252aa5e74210546f369fbbbce8c12cfc7957b2652fe9a75"
				.toCharArray();
		assertArrayEquals(expectedDigest, digest);
	}

	@Test
	public void testCreatePasswordDigestMultipleASCIIChar() throws Exception {
		char[] password = "password".toCharArray();
		char[] digest = PasswordUtility.getDigest(password, "SHA-512");
		char[] expectedDigest = "b109f3bbbc244eb82441917ed06d618b9008dd09b3befd1b5e07394c706a8bb980b1d7785e5976ec049b46df5f1326af5a2ea6d103fd07c95385ffab0cacbc86"
				.toCharArray();
		assertArrayEquals(expectedDigest, digest);
	}

	@Test
	public void testCreatePasswordDigestSingleLATINChar() throws Exception {
		// char[] password = { 'Ö' };
		// char[] digest = PasswordUtility.getDigest(password, "SHA-512");
		// char[] expectedDigest =
		// "874541ccab596c7f5a09b15acb9d02bd739d665d2223890f604dd6e7f0e66d3e9d79ed835626d9f6648938e8b7fae89382d560065863b935f4f234ac9a23fa0c"
		// .toCharArray();
		// assertArrayEquals(expectedDigest, digest);
	}

	@Test
	public void testCreatePasswordDigestMultipleLATINChar() throws Exception {
		// char[] password = "Ökonom".toCharArray();
		// char[] digest = PasswordUtility.getDigest(password, "SHA-512");
		// char[] expectedDigest =
		// "5b091b98937cdd36cf4476729fd37f79b6714a960e75c9ec31cc1231b9a0830a4f3dbe42a3a8bcea83957e9b11d1bd2652f6197756a1c63b357940cc3cc92803"
		// .toCharArray();
		// assertArrayEquals(expectedDigest, digest);
	}

	@Test
	public void testCreatePasswordDigestOfUTF16Surrogates() throws Exception {
		char[] password = "\ud835\udca5\ud835\udcb6\ud835\udccb\ud835\udcb6\ud835\udcc8\ud835\udcb8\ud835\udcc7\ud835\udcbe\ud835\udcc5\ud835\udcc9"
				.toCharArray();
		char[] digest = PasswordUtility.getDigest(password, "SHA-512");
		char[] expectedDigest = "f13cc619c2b16c2344e6bb863553217a1968fef898b900e45579b44fd224354e5d5e8b7d4ec03109370b7b110cd93f5d0916c909aee5c7ecf8604f3f7213be27"
				.toCharArray();
		assertArrayEquals(expectedDigest, digest);
	}

	@Test(expected = RuntimeException.class)
	public void testCreatePasswordDigestOfInvalidUTF16Sequence()
			throws Exception {
		char[] password = new String("\uDD00\uDD00").toCharArray();
		PasswordUtility.getDigest(password, "SHA-512");
		fail("Should not execute this line.");
	}

	@Test(expected = RuntimeException.class)
	public void testCreatePasswordDigestWithInvalidAlgorithm() throws Exception {
		char[] password = new String("\u0001").toCharArray();
		PasswordUtility.getDigest(password, "SHA-000");
		fail("Should not execute this line.");
	}
}
