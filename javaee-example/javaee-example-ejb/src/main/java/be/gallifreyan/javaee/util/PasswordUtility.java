package be.gallifreyan.javaee.util;

import java.nio.charset.CharacterCodingException;
import java.security.*;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.*;

public class PasswordUtility {

	private static final Logger logger = LoggerFactory
			.getLogger(PasswordUtility.class);

	private PasswordUtility() {
	}

	public static char[] getDigest(char[] password, String digestAlgorithm) {
		MessageDigest digester;
		char[] digest = null;
		try {
			byte[] inputBytes = PrimitiveArrayConverter
					.convertCharArrayToBytes(password, "UTF-8");

			digester = MessageDigest.getInstance(digestAlgorithm);
			digester.update(inputBytes);
			byte[] digestBytes = digester.digest();
			digest = Hex.encodeHex(digestBytes);
		} catch (NoSuchAlgorithmException noAlgEx) {
			logger.error(
					"No implementation of {} was found. Check your Java Security provider configuration.",
					digestAlgorithm, noAlgEx);
			throw new RuntimeException(noAlgEx);
		} catch (CharacterCodingException encodingEx) {
			logger.error(
					"Failed to decode the provided password into a sequence of UTF-8 bytes.",
					encodingEx);
			throw new RuntimeException(encodingEx);
		}
		return digest;
	}
}
