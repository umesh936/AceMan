package in.sminfo.tool.mgmt.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import in.sminfo.tool.mgmt.entity.SshAccessRecord;
import in.sminfo.tool.mgmt.entity.SshKeys;
import in.sminfo.tool.mgmt.entity.VpcHOP;
import in.sminfo.tool.mgmt.exception.GenericException;
import in.sminfo.tool.mgmt.repository.SshAccessRecordRepo;
import in.sminfo.tool.mgmt.repository.VpcHopRepository;
import in.sminfo.tool.mgmt.request.dto.RemoteSshRequestDTO;
import in.sminfo.tool.mgmt.request.dto.SshAccessUserRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RemoteSSHService {
	private static final String CLASS_NAME = "RemoteSSHService";

	@Resource
	VpcHopRepository vpcHopRepo;

	@Resource
	TokenService tokenService;

	@Resource
	ActionHistoryService actionHistoryService;

	@Resource
	SshAccessRecordRepo sshAccessRecordRepo;

	/**
	 * To check if access requested for instance is in same vpc or different
	 * vpc.
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws GenericException
	 */
	public void handleRemoteAccessRequestForGrant(SshAccessUserRequest sshRequest, SshKeys toSshUser,
			Integer awsAccountId) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, GenericException {
		VpcHOP hop = vpcHopRepo.findByVpcId(sshRequest.getVpcId());
		String token = tokenService.getToken(sshRequest.getInstanceId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("x-Auth", token);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		String completeUrl = "http://" + hop.getIp() + "/" + hop.getUrl();
		log.debug(CLASS_NAME + ": Hitting Url for remote access : " + completeUrl);
		RemoteSshRequestDTO requestDto = new RemoteSshRequestDTO();
		requestDto.setGroupName(sshRequest.getGroupName());
		requestDto.setIp(sshRequest.getInstanceIP());
		requestDto.setInstanceId(sshRequest.getInstanceId());
		requestDto.setKeyPairUserName(hop.getKeypair().getUserName());
		requestDto.setKeyPairFileName(hop.getKeypair().getKeypairName());
		requestDto.setSshUserName(toSshUser.getName());
		requestDto.setAccountId(awsAccountId.toString());
		ResponseEntity<String> response = restTemplate.exchange(completeUrl, HttpMethod.POST, entity, String.class);
		if (response.getStatusCodeValue() != 200) {
			throw new GenericException("Issue with Remote client, not able to get access.");
		}
	}

	public void handleRemoteAccessRequestForRevoke(SshAccessRecord sshAccessRecord, SshKeys sshUser)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException, GenericException {
		VpcHOP hop = vpcHopRepo.findByVpcId(sshAccessRecord.getVpcId());
		String token = tokenService.getToken(sshAccessRecord.getInstanceId());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("x-Auth", token);
		HttpEntity<?> entity = new HttpEntity<>(headers);
		RestTemplate restTemplate = new RestTemplate();
		String completeUrl = "http://" + hop.getIp() + "/" + hop.getUrl();
		log.debug(CLASS_NAME + ": Hitting Url for remote access : " + completeUrl);
		RemoteSshRequestDTO requestDto = new RemoteSshRequestDTO();
		requestDto.setIp(sshAccessRecord.getInstanceIP());
		requestDto.setKeyPairUserName(hop.getKeypair().getUserName());
		requestDto.setKeyPairFileName(hop.getKeypair().getKeypairName());
		requestDto.setSshUserName(sshUser.getName());
		requestDto.setAccountId(sshAccessRecord.getAwsAccountId().toString());
		ResponseEntity<String> response = restTemplate.exchange(completeUrl, HttpMethod.DELETE, entity, String.class);
		if (response.getStatusCodeValue() != 200) {
			throw new GenericException("Issue with Remote client, not able to get access.");
		}
	}
}
