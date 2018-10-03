package in.sminfo.tool.mgmt.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.common.utilities.CryptoHelp;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author umesh
 *
 */
@Slf4j
@Service
public class TokenService {

	@Value("${key:1234567890123456}")
	private String key16Char;

	public boolean verfiyToken(String token) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		String plaintoken = CryptoHelp.getPassPhraseDecrypt(token, key16Char);
		log.debug("PLain Token " + plaintoken);
		String tokenSplit[] = plaintoken.split(":::");
		LocalDateTime instant = LocalDateTime.parse(tokenSplit[0]);
		return isValidInet4Address(tokenSplit[1]) && instant.isAfter(LocalDateTime.now());
	}

	public String getToken(String ip) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		LocalDateTime instant = LocalDateTime.now();
		instant = instant.plusMinutes(30);
		return CryptoHelp.getPassPhraseEncrypt(instant + ":::" + ip, key16Char);
	}

	private boolean isValidInet4Address(String ip) {
		String[] groups = ip.split("\\.");

		if (groups.length != 4)
			return false;

		try {
			return Arrays.stream(groups).filter(s -> s.length() > 1 && s.startsWith("0")).map(Integer::parseInt)
					.filter(i -> (i >= 0 && i <= 255)).count() == 4;

		} catch (NumberFormatException e) {
			log.debug("isValidInet4Address : returning false");
			return false;
		}

	}

}
