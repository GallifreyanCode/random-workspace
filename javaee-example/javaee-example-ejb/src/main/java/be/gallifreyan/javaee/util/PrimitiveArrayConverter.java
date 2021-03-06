package be.gallifreyan.javaee.util;

import java.nio.*;
import java.nio.charset.*;
import java.util.Arrays;

public class PrimitiveArrayConverter {
	private PrimitiveArrayConverter() {
	}

	static byte[] convertCharArrayToBytes(char[] input, String charsetName)
			throws CharacterCodingException {
		CharBuffer charBuffer = CharBuffer.wrap(input);

		/*
		 * Creates a Charset encoder if the charset is supported and encodes the
		 * wrapped character array into a byte sequence.
		 */
		Charset charSet = Charset.forName(charsetName);
		CharsetEncoder encoder = charSet.newEncoder();
		ByteBuffer bytes = encoder.encode(charBuffer);

		/*
		 * Trim the contents of the byte buffer as the limit and capacity may
		 * not be the same. Clears the underlying byte array of the byte buffer
		 * before returning a copy.
		 */
		int limit = bytes.limit();
		byte[] bufferArray = bytes.array();
		byte[] result = Arrays.copyOf(bufferArray, limit);
		Arrays.fill(bufferArray, (byte) 0);

		return result;
	}

	static char[] convertByteArrayToChars(byte[] input, String charsetName)
			throws CharacterCodingException {
		ByteBuffer buffer = ByteBuffer.wrap(input);

		/*
		 * Creates a Charset decoder if the charset is supported and decodes the
		 * wrapped byte array into a character sequence.
		 */
		Charset charset = Charset.forName(charsetName);
		CharsetDecoder decoder = charset.newDecoder();
		CharBuffer chars = decoder.decode(buffer);

		/*
		 * Trim the contents of the char buffer as the limit and capacity may
		 * not be the same. Clears the underlying char array of the char buffer
		 * before returning a copy.
		 */
		int limit = chars.limit();
		char[] bufferArray = chars.array();
		char[] result = Arrays.copyOf(bufferArray, limit);
		Arrays.fill(bufferArray, '0');

		return result;
	}
}
