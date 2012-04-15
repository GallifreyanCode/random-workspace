package be.gallifreyan.javaee.util;

import java.nio.charset.CharacterCodingException;
import java.security.*;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.*;

public class PasswordUtility
{

	private static final Logger logger = LoggerFactory.getLogger(PasswordUtility.class);

	private PasswordUtility()
	{
	}

	/**
	 * Creates a digest of the provided password. The method first normalizes
	 * the UTF-16 codeunits provided via the character array into a UTF-8 byte
	 * sequence, which is then hashed using the specified algorithm.
	 * 
	 * @param password
	 *            The password whose digest is to be created.
	 * @param digestAlgorithm
	 *            The name of the algorithm to be used to create the digest. See
	 *            the JCA notes for valid algorithm names.
	 * @return The hex-encoded sequence representing the digest.
	 */
	public static char[] getDigest(char[] password, String digestAlgorithm)
	{
		MessageDigest digester;
		char[] digest = null;
		try
		{
			byte[] inputBytes = PrimitiveArrayConverter.convertCharArrayToBytes(password, "UTF-8");

			digester = MessageDigest.getInstance(digestAlgorithm);
			digester.update(inputBytes);
			byte[] digestBytes = digester.digest();
			digest = Hex.encodeHex(digestBytes);
		}
		catch (NoSuchAlgorithmException noAlgEx)
		{
			logger.error("No implementation of {} was found. Check your Java Security provider configuration.",
					digestAlgorithm, noAlgEx);
			throw new RuntimeException(noAlgEx);
		}
		catch (CharacterCodingException encodingEx)
		{
			logger.error("Failed to decode the provided password into a sequence of UTF-8 bytes.", encodingEx);
			throw new RuntimeException(encodingEx);
		}
		return digest;
	}
}
