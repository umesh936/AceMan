package in.sminfo.tool.mgmt.controller;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.services.TokenService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/token/verify")
public class TokenController {

	@Value("${key:1234567890123456}")
	private String key16Char;
	@Resource
	TokenService tokenService;

	@GetMapping("")
	public void verifyToken(@RequestHeader("x-Auth") String xAuth) throws Exception {
		tokenService.verfiyToken(xAuth);
	}

	/**
	 * 
	 * @param ip
	 *            - ip of machines
	 * @return
	 * 
	 *         <Pre>
	 *  200 -  token verified
	 *         </pre>
	 * 
	 * @throws  InvalidKeyException {@link
	 *             InvalidKeyException}
	 * @throws NoSuchAlgorithmException {@link
	 *             NoSuchAlgorithmException }
	 * @throws NoSuchPaddingException {@link
	 *             NoSuchPaddingException }
	 * @throws IllegalBlockSizeException {@link
	 *             IllegalBlockSizeException}
	 * @throws  BadPaddingException {@link
	 *             BadPaddingException}
	 * @throws UnsupportedEncodingException {@link
	 *             UnsupportedEncodingException}
	 */
	@PostMapping("")
	public ResponseEntity<?> getToken(@RequestHeader("x-ip") String ip)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		log.debug("Token generation request came along for :" + ip);
		return new ResponseEntity<String>(tokenService.getToken(ip), HttpStatus.OK);
	}
}
