package in.sminfo.tool.mgmt.common.utilities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.Data;

/**
 * Class responsible for Encryption/Decryption , Hashing Help.
 * 
 * @author umesh
 *
 */
@Data
public class CryptoHelp {
	/**
	 * Return Encrypted String for given text and key.
	 * 
	 * @param passPhrase
	 *            - password to encypt
	 * @param key16Char
	 *            - key ro encrypt
	 * @return encrypted Password
	 * @throws NoSuchAlgorithmException
	 *             {@link NoSuchAlgorithmException}
	 * @throws NoSuchPaddingException
	 *             {@link NoSuchPaddingException}
	 * @throws InvalidKeyException
	 *             {@link InvalidKeyException}
	 * @throws IllegalBlockSizeException
	 *             {@link IllegalBlockSizeException}
	 * @throws BadPaddingException
	 *             {@link BadPaddingException}
	 * @throws UnsupportedEncodingException
	 *             {@link UnsupportedEncodingException}
	 */
	public static String getPassPhraseEncrypt(String passPhrase, String key16Char)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		// Create key and cipher
		Key aesKey = new SecretKeySpec(key16Char.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		// encrypt the text
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(passPhrase.getBytes());
		return (new String(encrypted));

	}

	/**
	 * Return Decrypted String for given text and key.
	 * 
	 * @param encryptStr
	 *            password to decrypt
	 * @param key16Char
	 *            key to decrypt
	 * @return decrypted password
	 * @throws NoSuchAlgorithmException
	 *             {@link NoSuchAlgorithmException}
	 * @throws InvalidKeyException
	 *             {@link InvalidKeyException}
	 * @throws IllegalBlockSizeException
	 *             {@link IllegalBlockSizeException}
	 * @throws BadPaddingException
	 *             {@link BadPaddingException}
	 * @throws NoSuchPaddingException
	 *             {@link NoSuchPaddingException}
	 */
	public static String getPassPhraseDecrypt(String encryptStr, String key16Char) throws NoSuchAlgorithmException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		// Create key and cipher
		Key aesKey = new SecretKeySpec(key16Char.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES");
		// decrypt the text
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		String decrypted = new String(cipher.doFinal(encryptStr.getBytes()));
		return decrypted;
	}

	/**
	 * Get password hashed.
	 * 
	 * @param password
	 *            password to hashed
	 * @param salt
	 *            - salt to hash
	 * @return hashed password
	 * @throws NoSuchAlgorithmException
	 *             {@link NoSuchAlgorithmException}
	 * @throws InvalidKeySpecException
	 *             {@link InvalidKeySpecException}
	 */
	public static String generateStorngPasswordHash(String password, String salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		int iterations = 1000;
		char[] chars = password.toCharArray();
		PBEKeySpec spec = new PBEKeySpec(chars, salt.getBytes(), iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] hash = skf.generateSecret(spec).getEncoded();
		return iterations + ":" + toHex(salt.getBytes()) + ":" + toHex(hash);
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	// public static void main(String[] args) throws Exception {
	// System.out.println(generateStorngPasswordHash("umesh123",
	// "1234567890123456"));
	// }
}
