package in.sminfo.tool.mgmt.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.entity.SshAccessRecord;
import in.sminfo.tool.mgmt.enums.UserType;
import in.sminfo.tool.mgmt.exception.InvalidRequestException;
import in.sminfo.tool.mgmt.request.dto.SshAccessUserRequest;
import in.sminfo.tool.mgmt.request.dto.SshKeyRequestObject;
import in.sminfo.tool.mgmt.resposne.dto.SshKeysResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.services.SshService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ssh/access")
public class SshKeyAccessController {
	@Resource
	SshService sshService;
	@Resource
	ObjectMapper objectMapper;

	@SneakyThrows
	@GetMapping("/users")
	public ResponseEntity<?> getSshUserAccessList(HttpServletRequest request, HttpServletResponse response) {
		log.debug("SshUserAccessController-getSshUserAccessList:List All the user.");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		return new ResponseEntity<List<UserObject>>(sshService.getUsersList(), HttpStatus.OK);
	}

	// 14 - aug for get only active ssh key of users
	@SneakyThrows
	@GetMapping("/activeusers")
	public ResponseEntity<?> getActiveSshUserAccessList(HttpServletRequest request, HttpServletResponse response) {
		log.debug("SshUserAccessController-getSshUserAccessList:List All the user.");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		return new ResponseEntity<List<UserObject>>(sshService.getActiveUsersList(), HttpStatus.OK);
	}

	@SneakyThrows
	@PostMapping("/users/keys")
	public ResponseEntity<?> saveSshUserKey(@RequestBody SshKeyRequestObject sshKeyRequest, HttpServletRequest request,
			HttpServletResponse response) {
		log.debug("SshUserAccessController-saveSshUser:");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<Map<String, String>>(sshService.saveUserKey(user, sshKeyRequest), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/users/keys")
	public ResponseEntity<?> fetchSshUserKey(HttpServletRequest request, HttpServletResponse response) {
		log.debug("SshUserAccessController-fetchSshUserKey:");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<List<SshKeysResponseObject>>(sshService.getSshUserKeysById(user), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/users/{userid}")
	public ResponseEntity<?> getAccessDetailsForUser(@PathVariable("userid") Integer userId, HttpServletRequest request,
			HttpServletResponse response) {
		log.debug("SshUserAccessController-getAccessDetailsForUser: Getting  access Details to " + userId
				+ " for Ip this. ");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<SshAccessRecord>>(sshService.getSshAccessBySShUserId(userId, awsAccountId),
				HttpStatus.OK);
	}

	@SneakyThrows
	@PostMapping("/users/{touserId}")
	public ResponseEntity<?> giveAccessToUser(@PathVariable("touserId") Integer toUId,
			@RequestBody SshAccessUserRequest userRequest, HttpServletRequest request, HttpServletResponse response) {
		log.debug("SshUserAccessController-giveAccessToUser:Giving access to " + toUId + " for Instance this. "
				+ userRequest.getInstanceId());
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject fromUser = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (fromUser == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		if (!UserType.canGiveSshAccess(UserType.valueOf(fromUser.getUserType())))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		sshService.giveAccessToUserForInstance(userRequest, toUId, fromUser, awsAccountId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@SneakyThrows
	@DeleteMapping("/users/{touserId}/{instanceid}")
	public ResponseEntity<?> deleteAccessToUser(@PathVariable("touserId") Integer toUId, @PathVariable("instanceid") String instanceId,
			 HttpServletRequest request, HttpServletResponse response) {
		log.debug("SshUserAccessController-giveAccessToUser:Giving access to " + toUId + " for Instance this. "
				+ instanceId);
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject fromUser = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (fromUser == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		if (!UserType.canGiveSshAccess(UserType.valueOf(fromUser.getUserType())))
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		sshService.deleteAccess(
				sshService.getSshAccessBySShUserIdAndInstanceId(toUId, awsAccountId, instanceId),
				fromUser.getName());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/instance/{instanceid}")
	public ResponseEntity<?> getAccessDetailsForInstance(@PathVariable("instanceid") String instanceid,
			HttpServletRequest request, HttpServletResponse response) {
		log.debug("SshUserAccessController-giveAccessToUser:Get access Details for Instance " + instanceid);
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute("awsAccountId");
		return new ResponseEntity<List<UserObject>>(sshService.getSshAccessByInstanceId(instanceid, awsAccountId),
				HttpStatus.OK);

	}

}
