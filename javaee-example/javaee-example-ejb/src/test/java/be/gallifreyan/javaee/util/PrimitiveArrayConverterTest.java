package be.gallifreyan.javaee.util;

import static org.junit.Assert.*;

import java.nio.charset.UnsupportedCharsetException;

import org.junit.Test;

public class PrimitiveArrayConverterTest {

	private static final byte DECIMAL_0XFF = (byte) (0xFF);
	private static final byte DECIMAL_0XFE = (byte) (0xFE);

	private static final char LATIN_SMALL_LETTER_B = 'b';
	private static final char LATIN_SMALL_LETTER_A = 'a';

	private static final int DECIMAL_0 = 0;
	private static final int DECIMAL_98 = 98;
	private static final int DECIMAL_97 = 97;

	private static final String UTF_8 = "UTF-8";
	private static final String UTF_16 = "UTF-16";
	private static final String UTF_16BE = "UTF-16BE";
	private static final String UTF_16LE = "UTF-16LE";

	@Test(expected = NullPointerException.class)
	public void testConvertNullCharsToBytes() throws Exception {
		PrimitiveArrayConverter.convertCharArrayToBytes(null, UTF_8);
		fail("Should not have executed this statement.");
	}

	@Test
	public void testConvertNoCharsToBytes() throws Exception {
		char[] input = {};
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_8);
		assertNotNull(bytes);
		byte[] expectedBytes = {};
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test
	public void testConvertSingleCharToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A };
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_8);
		assertNotNull(bytes);
		byte[] expectedBytes = { DECIMAL_97 };
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test
	public void testConvertTwoCharsToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A, LATIN_SMALL_LETTER_B };
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_8);
		assertNotNull(bytes);
		byte[] expectedBytes = { DECIMAL_97, DECIMAL_98 };
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test
	public void testConvertTwoUTF16BECharsToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A };
		// Specify UTF-16BE as the encoding, otherwise the output will contain a
		// BOM
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_16BE);
		assertNotNull(bytes);
		byte[] expectedBytes = { DECIMAL_0, DECIMAL_97 };
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test
	public void testConvertTwoUTF16LECharsToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A };
		// Specify UTF-16LE as the encoding, otherwise the output will contain a
		// BOM
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_16LE);
		assertNotNull(bytes);
		byte[] expectedBytes = { DECIMAL_97, DECIMAL_0 };
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test
	public void testConvertOneUTF16CharToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A };
		// Specify UTF-16LE as the encoding, otherwise the output will contain a
		// BOM
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_16);
		assertNotNull(bytes);
		byte[] expectedBytes = { DECIMAL_0XFE, DECIMAL_0XFF, DECIMAL_0,
				DECIMAL_97 };
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test
	public void testConvertTwoUTF16CharsToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A, LATIN_SMALL_LETTER_B };
		byte[] bytes = PrimitiveArrayConverter.convertCharArrayToBytes(input,
				UTF_16);
		assertNotNull(bytes);
		byte[] expectedBytes = { DECIMAL_0XFE, DECIMAL_0XFF, DECIMAL_0,
				DECIMAL_97, DECIMAL_0, DECIMAL_98 };
		assertArrayEquals(expectedBytes, bytes);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertCharsOfNullCharsetToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A };
		PrimitiveArrayConverter.convertCharArrayToBytes(input, null);
		fail("Should not have executed this statement.");
	}

	@Test(expected = UnsupportedCharsetException.class)
	public void testConvertCharsOfUnknownCharsetToBytes() throws Exception {
		char[] input = { LATIN_SMALL_LETTER_A };
		PrimitiveArrayConverter.convertCharArrayToBytes(input, "UTF-64");
		fail("Should not have executed this statement.");
	}

	@Test(expected = NullPointerException.class)
	public void testConvertNullBytesToChars() throws Exception {
		PrimitiveArrayConverter.convertByteArrayToChars(null, UTF_8);
		fail("Should not have executed this statement.");
	}

	@Test
	public void testConvertNoBytesToChars() throws Exception {
		byte[] input = {};
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_8);
		assertNotNull(chars);
		char[] expectedChars = {};
		assertArrayEquals(expectedChars, chars);
	}

	@Test
	public void testConvertSingleByteToChars() throws Exception {
		byte[] input = { DECIMAL_97 };
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_8);
		assertNotNull(chars);
		char[] expectedChars = { LATIN_SMALL_LETTER_A };
		assertArrayEquals(expectedChars, chars);
	}

	@Test
	public void testConvertTwoBytesToChars() throws Exception {
		byte[] input = { DECIMAL_97, DECIMAL_98 };
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_8);
		assertNotNull(chars);
		char[] expectedChars = { LATIN_SMALL_LETTER_A, LATIN_SMALL_LETTER_B };
		assertArrayEquals(expectedChars, chars);
	}

	@Test
	public void testConvertTwoUTF16BEBytesToChars() throws Exception {
		byte[] input = { DECIMAL_0, DECIMAL_97 };
		// Specify UTF-16BE as the encoding, otherwise the output will contain a
		// BOM
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_16BE);
		assertNotNull(chars);
		char[] expectedChars = { LATIN_SMALL_LETTER_A };
		assertArrayEquals(expectedChars, chars);
	}

	@Test
	public void testConvertTwoUTF16LEBytesToChars() throws Exception {
		byte[] input = { DECIMAL_97, DECIMAL_0 };
		// Specify UTF-16LE as the encoding, otherwise the output will contain a
		// BOM
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_16LE);
		assertNotNull(chars);
		char[] expectedChars = { LATIN_SMALL_LETTER_A };
		assertArrayEquals(expectedChars, chars);
	}

	@Test
	public void testConvertOneUTF16ByteToChars() throws Exception {
		byte[] input = { DECIMAL_0XFE, DECIMAL_0XFF, DECIMAL_0, DECIMAL_97 };
		// Specify UTF-16LE as the encoding, otherwise the output will contain a
		// BOM
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_16);
		assertNotNull(chars);
		char[] expectedChars = { LATIN_SMALL_LETTER_A };
		assertArrayEquals(expectedChars, chars);
	}

	@Test
	public void testConvertTwoUTF16BytesToChars() throws Exception {
		byte[] input = { DECIMAL_0XFE, DECIMAL_0XFF, DECIMAL_0, DECIMAL_97,
				DECIMAL_0, DECIMAL_98 };
		char[] chars = PrimitiveArrayConverter.convertByteArrayToChars(input,
				UTF_16);
		assertNotNull(chars);
		char[] expectedChars = { LATIN_SMALL_LETTER_A, LATIN_SMALL_LETTER_B };
		assertArrayEquals(expectedChars, chars);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConvertBytesOfNullCharsetToChars() throws Exception {
		byte[] input = { DECIMAL_97 };
		PrimitiveArrayConverter.convertByteArrayToChars(input, null);
		fail("Should not have executed this statement.");
	}

	@Test(expected = UnsupportedCharsetException.class)
	public void testConvertBytesOfUnknownCharsetToChars() throws Exception {
		byte[] input = { DECIMAL_97 };
		PrimitiveArrayConverter.convertByteArrayToChars(input, "UTF-64");
		fail("Should not have executed this statement.");
	}
}
