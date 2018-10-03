package in.sminfo.tool.mgmt.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jcraft.jsch.Session;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.common.utilities.StaticDataHolder;
import in.sminfo.tool.mgmt.entity.KeyPair;
import in.sminfo.tool.mgmt.entity.SshAccessRecord;
import in.sminfo.tool.mgmt.entity.SshKeys;
import in.sminfo.tool.mgmt.entity.UserEntity;
import in.sminfo.tool.mgmt.entity.VpcHOP;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import in.sminfo.tool.mgmt.exception.UserAlreadyHaveAccessException;
import in.sminfo.tool.mgmt.repository.KeyPairRepo;
import in.sminfo.tool.mgmt.repository.SshAccessRecordRepo;
import in.sminfo.tool.mgmt.repository.SshUserRepo;
import in.sminfo.tool.mgmt.repository.VpcHopRepository;
import in.sminfo.tool.mgmt.request.dto.SshAccessUserRequest;
import in.sminfo.tool.mgmt.request.dto.SshKeyRequestObject;
import in.sminfo.tool.mgmt.resposne.dto.InstanceObject;
import in.sminfo.tool.mgmt.resposne.dto.SshKeysResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SshService {
	private static final String CLASS_NAME = "SshService :";
	@Resource
	SSHChannelInterface sshChannelService;

	@Resource
	SshUserRepo sshUserRepo;

	@Resource
	KeyPairRepo keyPairRepo;

	@Resource
	SshAccessRecordRepo sshAccessRecordRepo;

	@Resource
	UIUserService uiUserService;

	@Resource
	VpcHopRepository vpcHopRepo;

	@Resource
	TokenService tokenService;

	@Resource
	RemoteSSHService remoteSshService;

	@Resource
	ActionHistoryService actionHistoryService;
	@Resource
	AwsEc2Services ec2Service;
	@Resource
	VaultService vaultService;

	public void deleteAccess(SshAccessRecord sshRecord, String fromUser) throws Exception {
		UserEntity user = uiUserService.findUserEntityById(sshRecord.getUid());
		SshKeys sshUser = sshUserRepo.findByUserAndIsActive(user, true);
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.SSH_ACCESS);
		event.setFromUser(fromUser);
		event.setToUser(String.valueOf(sshUser.getUser().getUname()));
		event.setDescription("Revoking SSH Access For Ip :" + sshRecord.getInstanceIP());
		Integer id = actionHistoryService.logEvent(event);
		if (!sshRecord.getVpcId().equals(StaticDataHolder.getData(Constant.DATA.SELF_VPC))) {
			remoteSshService.handleRemoteAccessRequestForRevoke(sshRecord, sshUser);
			return;
		} else {
			if (!sshRecord.getInstanceId().equals(StaticDataHolder.getData(Constant.DATA.SELF_INSTANCE_ID))) {
				String command = "> /home/" + sshUser.getName() + "/.ssh/authorized_keys";
				Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
				p.waitFor();
				BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				StringBuffer sb = new StringBuffer();
				String line = "";
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				log.debug(CLASS_NAME + "DeleteAccess: output for local =");
			} else {
				Session session = sshChannelService.getSession(sshRecord.getInstanceIP(), getKeyPair(sshRecord));
				sshChannelService.connectAndExecute(session, ">  /home/" + sshUser.getName() + "/.ssh/authorized_keys");
				log.debug(CLASS_NAME + "DeleteAccess: Access Revoke to user " + sshUser.getName() + " for IP "
						+ sshRecord.getInstanceIP());
			}
		}
		sshRecord.setIsExpired(true);
		sshAccessRecordRepo.saveAndFlush(sshRecord);
		actionHistoryService.updateEventStatus(id, true);
	}

	private KeyPair getKeyPair(SshAccessRecord sshRecord) {
		VpcHOP vpcHop = vpcHopRepo.findByVpcId(sshRecord.getVpcId());
		return vpcHop.getKeypair();
	}

	@SneakyThrows
	public void giveAccessToUserForInstance(SshAccessUserRequest sshRequest, Integer toUserId, UserObject fromUserid,
			Integer awsAccountId) {
		if (isUserExistsWithStatus(sshRequest.getInstanceId(), toUserId, false)) {
			throw new UserAlreadyHaveAccessException(
					"User " + toUserId + " Already have access to " + sshRequest.getInstanceId());
		}
		sshRequest.setKeyPairName(
				getKeyPairNameForInstance(sshRequest.getInstanceId(), awsAccountId, sshRequest.getRegion()));
		UserEntity user = uiUserService.findUserEntityById(toUserId);
		SshKeys sshUser = sshUserRepo.findByUserAndIsActive(user, true);
		if (sshUser == null)
			throw new ObjectNotFoundException("Ssh User Not Found with this Id " + toUserId);
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.SSH_ACCESS);
		event.setFromUser(String.valueOf(fromUserid.getName()));
		event.setToUser(String.valueOf(sshUser.getUser().getUname()));
		event.setDescription("Giving SSH Access For Ip :" + sshRequest.getInstanceIP());
		Integer id = actionHistoryService.logEvent(event);
		if (!sshRequest.getVpcId().equals(StaticDataHolder.getData(Constant.DATA.SELF_VPC))) {
			remoteSshService.handleRemoteAccessRequestForGrant(sshRequest, sshUser, awsAccountId);
		} else {
			handleSameVPCRequestForGrant(sshRequest, awsAccountId, sshUser);
		}
		actionHistoryService.updateEventStatus(id, true);
		saveSSHRecordForGrant(sshRequest, awsAccountId, fromUserid.getId(), toUserId);
	}

	private String getKeyPairNameForInstance(String instanceId, Integer awsAccountId, String region) {
		List<String> instanceIds = new ArrayList<>();
		instanceIds.add(instanceId);
		List<InstanceObject> instanceObjectList = ec2Service.describeEc2InstanceByIds(instanceIds, awsAccountId,
				region);
		return instanceObjectList.get(0).getKeyName();
	}

	@SneakyThrows
	private void handleSameVPCRequestForGrant(SshAccessUserRequest sshRequest, int awsAccountId, SshKeys sshUser) {
		if (sshRequest.getInstanceId().equals(StaticDataHolder.getData(Constant.DATA.SELF_INSTANCE_ID))) {
			log.debug(CLASS_NAME + "access required for same instance Id " + sshRequest.getInstanceId());
			String appendKeyTofile = "sudo echo '" + sshUser.getSshKey() + "' >>  /home/" + sshUser.getName()
					+ "/.ssh/authorized_keys";
			log.debug(CLASS_NAME + "command to run on same machine " + appendKeyTofile);
			// Build command
			List<String> commands = new ArrayList<String>();
			commands.add("sudo useradd -m " + sshUser.getName());
			commands.add("sudo mkdir /home/" + sshUser.getName() + "/.ssh");
			commands.add("sudo touch /home/" + sshUser.getName() + "/.ssh/authorized_keys");
			commands.add("sudo chown" + sshUser.getName() + ":" + sshUser.getName() + " -R  /home/" + sshUser.getName()
					+ "/.ssh");
			commands.add(appendKeyTofile);
			for (String command : commands) {
				Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command });
				p.waitFor();
				BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				// read any errors from the attempted command
				log.debug(CLASS_NAME
						+ "handleSameVPCRequestForGrant: Here is the standard error of the command (if any):\n");
				String s = null;
				while ((s = stdError.readLine()) != null) {
					log.debug(CLASS_NAME + "handleSameVPCRequestForGrant: output for local =" + s);
				}
				log.debug(CLASS_NAME + "handleSameVPCRequestForGrant: exist value for process =" + p.exitValue());
			}

		} else {
			KeyPair keyPair = keyPairRepo.findByAwsAccountIdAndKeypairName(awsAccountId, sshRequest.getKeyPairName());
			Session session = sshChannelService.getSession(sshRequest.getInstanceIP(), keyPair);
			sshChannelService.connectAndExecute(session,
					"sudo useradd -m -G " + sshRequest.getGroupName() + " " + sshUser.getName());
			sshChannelService.connectAndExecute(session, "sudo mkdir /home/" + sshUser.getName() + "/.ssh");
			sshChannelService.scpFile(sshRequest.getInstanceIP(), keyPair, sshUser);
			sshChannelService.connectAndExecute(session,
					"sudo mv " + sshUser.getName() + " /home/" + sshUser.getName() + "/.ssh/authorized_keys");
			sshChannelService.connectAndExecute(session, "sudo chown -R " + sshUser.getName() + ":" + sshUser.getName()
					+ " /home/" + sshUser.getName() + "/.ssh");
			session.disconnect();
		}
	}

	private void saveSSHRecordForGrant(SshAccessUserRequest sshRequest, Integer accountId, int fromUserId,
			int toUserId) {
		SshAccessRecord sshAccessRecord = new SshAccessRecord();
		sshAccessRecord.setAwsAccountId(accountId);
		sshAccessRecord.setEventDate(new Date());
		sshAccessRecord.setInstanceId(sshRequest.getInstanceId());
		sshAccessRecord.setGivenByUser(fromUserId);
		sshAccessRecord.setUid(toUserId);
		sshAccessRecord.setVpcId(sshRequest.getVpcId());
		sshAccessRecord.setInstanceIP(sshRequest.getInstanceIP());
		sshAccessRecord.setIsExpired(false);
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, sshRequest.getNoOfDays());
		sshAccessRecord.setExpiryDate(c.getTime());
		sshAccessRecordRepo.saveAndFlush(sshAccessRecord);
	}

	private boolean isUserExistsWithStatus(String instanceId, Integer toUserId, Boolean isExpire) {
		List<SshAccessRecord> userRecod = sshAccessRecordRepo.findByInstanceIdAndUidAndIsExpired(instanceId, toUserId,
				isExpire);
		if (userRecod == null || userRecod.size() == 0)
			return false;
		else
			return true;
	}

	public List<UserObject> getUsersList() {
		List<SshKeys> sshUserList = sshUserRepo.findAll();
		List<UserObject> listToSent = new ArrayList<UserObject>();
		for (SshKeys sshUser : sshUserList) {
			listToSent.add(uiUserService.convertUserEntityToUserObject(sshUser.getUser()));
		}
		return listToSent;
	}

	public List<UserObject> getActiveUsersList() {
		List<SshKeys> sshUserList = sshUserRepo.findByIsActive(true);
		List<UserObject> listToSent = new ArrayList<UserObject>();
		for (SshKeys sshUser : sshUserList) {
			listToSent.add(uiUserService.convertUserEntityToUserObject(sshUser.getUser()));
		}
		return listToSent;
	}

	public List<SshKeysResponseObject> getSshUserKeysById(UserObject user) {
		List<SshKeysResponseObject> listToSend = new ArrayList<>();
		List<SshKeys> sshKeyList = getAllKeysForUser(user);
		for (SshKeys key : sshKeyList) {
			if (key.getSshKey().length() > 20) {
				key.setSshKey(key.getSshKey().substring(0, 35) + "...");
			}
			listToSend.add(convertSshKeyEntityToObject(key));
		}
		return listToSend;
	}

	@SneakyThrows
	public List<SshAccessRecord> getSshAccessBySShUserId(Integer uid, Integer accountId) {
		List<SshAccessRecord> accessRecordList = sshAccessRecordRepo.findByUidAndAwsAccountIdAndIsExpired(uid,
				accountId, false);
		return accessRecordList;
	}

	@SneakyThrows
	public SshAccessRecord getSshAccessBySShUserIdAndInstanceId(Integer uid, Integer awsAccountId, String instanceId) {
		SshAccessRecord accessRecord = sshAccessRecordRepo.findByInstanceIdAndUidAndAwsAccountIdAndIsExpired(instanceId,
				uid, awsAccountId, false);
		return accessRecord;
	}

	@SneakyThrows
	public List<UserObject> getSshAccessByInstanceId(String instanceId, Integer awsAccountId) {
		List<UserObject> listTosend = new ArrayList<>();
		log.debug(CLASS_NAME + "getSshAccessByInstanceId: " + instanceId);
		List<SshAccessRecord> accessRecordList = sshAccessRecordRepo
				.findByInstanceIdAndAwsAccountIdAndIsExpired(instanceId, awsAccountId, false);
		for (SshAccessRecord sshAccessRecord : accessRecordList) {
			log.debug(CLASS_NAME + "getSshAccessByInstanceId: finding user by Id " + sshAccessRecord.getUid());
			listTosend.add(uiUserService.findUserById(sshAccessRecord.getUid()));
		}
		return listTosend;
	}

	public Map<String, String> saveUserKey(UserObject user, SshKeyRequestObject sshkeyRequest) {
		Map<String, String> resultMap = new HashMap<>();
		SshKeys oldObject = sshUserRepo.findByName(sshkeyRequest.getLogicalName());
		if (oldObject != null) {
			resultMap.put("msg",
					"Name  already present in database , please enter another name:" + sshkeyRequest.getLogicalName());
			return resultMap;
		}
		List<SshKeys> sshKeyList = getAllKeysForUser(user);
		if (sshKeyList != null) {
			for (SshKeys sshKeys : sshKeyList) {
				String keys = vaultService.readSSHKeys(sshKeys.getName());
				if (keys.equals(sshkeyRequest.getKey())) {
					sshKeys.setIsActive(true);
					sshUserRepo.saveAndFlush(sshKeys);
					resultMap.put("msg", "Key already present with name :" + sshKeys.getName());
					return resultMap;
				}
				sshKeys.setIsActive(false);
				sshUserRepo.saveAndFlush(sshKeys);
			}
		}

		SshKeys keys = new SshKeys();
		keys.setName(sshkeyRequest.getLogicalName());
		vaultService.writeSSHKeys(sshkeyRequest.getLogicalName(), sshkeyRequest.getKey());
		;
		keys.setUser(uiUserService.findUserEntityById(user.getId()));
		keys.setIsActive(true);
		sshUserRepo.saveAndFlush(keys);
		resultMap.put("msg", "ok");
		return resultMap;
	}

	private List<SshKeys> getAllKeysForUser(UserObject user) {
		UserEntity userEntity = uiUserService.findUserEntityById(user.getId());
		List<SshKeys> sshKeyList = sshUserRepo.findByUser(userEntity);
		return sshKeyList;
	}

	private SshKeysResponseObject convertSshKeyEntityToObject(SshKeys key) {
		SshKeysResponseObject objectToSend = new SshKeysResponseObject();
		objectToSend.setId(key.getId());
		objectToSend.setName(key.getName());
		objectToSend.setSshKey(key.getSshKey());
		return objectToSend;
	}

}
