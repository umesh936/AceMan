package in.sminfo.tool.mgmt.services;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.common.utilities.CryptoHelp;
import in.sminfo.tool.mgmt.entity.UserEntity;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.enums.UserType;
import in.sminfo.tool.mgmt.exception.GenericRuntimeException;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import in.sminfo.tool.mgmt.repository.UserRepository;
import in.sminfo.tool.mgmt.request.dto.UserRequestObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UIUserService {
	private static final String CLASS_NAME = "UIUserService: ";

	@Value("${key:1234567890123456}")
	private String key16Char;

	@Resource
	private UserRepository userRepo;

	@Resource
	ActionHistoryService historyService;
	
	@Resource
	TeamService teamservice;

	public UserObject loginAllowed(String email, String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		String passPhrase = null;
		try {

			passPhrase = CryptoHelp.generateStorngPasswordHash(password, key16Char);
		} catch (Exception e) {
			throw new GenericRuntimeException("Some Issue with Password.");
		}
		System.out.println(passPhrase);
		UserEntity userEntity = userRepo.findByEmailAndPassword(email, passPhrase);
		if (userEntity != null)
			return convertUserEntityToUserObject(userEntity);
		else
			throw new GenericRuntimeException("Login not Allowed.");
	}

	public UserObject googleLoginAllowed(String email, String googleUid, String Uname) {
		if (!email.contains("@mettl.com")) {
			return null;
		}
		UserEntity userEntity = userRepo.findByEmailAndGoogleUid(email, googleUid);
		if (userEntity != null)
			return convertUserEntityToUserObject(userEntity);
		else {
			UserEntity newuserEntity = new UserEntity();
			newuserEntity.setCreatedOn(new Date());
			newuserEntity.setEmail(email);
			newuserEntity.setGoogleUid(googleUid);
			newuserEntity.setIsActive(true);
			newuserEntity.setUserType(UserType.NORMAL);
			newuserEntity.setUname(Uname);
			newuserEntity.setDeletedAt(null);
			newuserEntity = userRepo.saveAndFlush(newuserEntity);
			return convertUserEntityToUserObject(newuserEntity);
		}
	}

	public void register(UserRequestObject userObject) throws NoSuchAlgorithmException, InvalidKeySpecException {
		UserEntity user = userRepo.findByUname(userObject.getName());
		if (user == null) {
			user = new UserEntity();
			user.setCreatedOn(new Date());
			user.setIsActive(true);
			user.setUname(userObject.getName());
			user.setEmail(userObject.getEmail());
			user.setPassword(CryptoHelp.generateStorngPasswordHash(userObject.getPassword(), key16Char));
			userRepo.saveAndFlush(user);
		} else
			throw new GenericRuntimeException("User Already in Database.");
	}

	@SneakyThrows(ObjectNotFoundException.class)
	public UserObject findUserById(Integer Id) {
		Optional<UserEntity> option = userRepo.findById(Id);
		if (!option.isPresent())
			throw new ObjectNotFoundException("User Not found with Id :" + Id);
		UserEntity userEntity = option.get();
		return convertUserEntityToUserObject(userEntity);
	}

	@SneakyThrows(ObjectNotFoundException.class)
	public UserEntity findUserEntityById(Integer Id) {
		Optional<UserEntity> option = userRepo.findById(Id);
		if (!option.isPresent())
			throw new ObjectNotFoundException("User Not found with Id :" + Id);
		UserEntity userEntity = option.get();
		return userEntity;
	}

	@SneakyThrows
	public Boolean canUserCreateTeam(Integer Id) {
		UserObject user = findUserById(Id);
		return UserType.canCreateTeam(UserType.valueOf(user.getUserType()));
	}

	@SneakyThrows
	public Boolean canUserUpdateTeam(Integer Id) {
		UserObject user = findUserById(Id);
		return UserType.canUpdateTeam(UserType.valueOf(user.getUserType()));
	}

	/**
	 * List All User
	 * 
	 * @return
	 */

	@SneakyThrows
	public List<UserObject> findAllActiveUser() {
		List<UserEntity> userList = userRepo.findByIsActive(true);
		List<UserObject> userObjectList = new ArrayList<UserObject>();
		if (userList == null || userList.size() == 0)
			throw new ObjectNotFoundException();
		for (UserEntity userEntity : userList) {
			userObjectList.add(convertUserEntityToUserObject(userEntity));
		}
		return userObjectList;

	}

	@SneakyThrows
	public List<UserObject> findAllUser() {
		List<UserEntity> userList = userRepo.findAll();
		List<UserObject> userObjectList = new ArrayList<UserObject>();
		if (userList == null || userList.size() == 0)
			throw new ObjectNotFoundException();
		for (UserEntity userEntity : userList) {
			userObjectList.add(convertUserEntityToUserObject(userEntity));
		}
		return userObjectList;

	}

	@SneakyThrows
	public List<UserObject> findAllManagers(UserType type) {
		List<UserEntity> userList = userRepo.findByUserTypeAndIsActiveAndTeamId(type, true, null);
		List<UserObject> userObjectList = new ArrayList<UserObject>();
		if (userList == null || userList.size() == 0)
			throw new ObjectNotFoundException();
		for (UserEntity userEntity : userList) {
			userObjectList.add(convertUserEntityToUserObject(userEntity));
		}
		return userObjectList;

	}

	public List<UserObject> findAllUserByTeamId(Integer teamId) throws ObjectNotFoundException {
		List<UserEntity> userList = userRepo.findByTeamId(teamId);
		List<UserObject> userObjectList = new ArrayList<UserObject>();

		if (userList == null || userList.size() == 0)
			throw new ObjectNotFoundException();
		for (UserEntity userEntity : userList) {
			userObjectList.add(convertUserEntityToUserObject(userEntity));
		}
		return userObjectList;

	}

	public UserObject changeEmail(UserObject user, String newEmail) {
		UserEntity userEntity = userRepo.findById(user.getId()).get();
		userEntity.setEmail(newEmail);
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully change user Email for user- " + userEntity.getUid());
		return convertUserEntityToUserObject(userEntity);
	}

	public UserObject addTeamId(Integer id, Integer newgid, String teamName, UserObject user) {
		UserEntity userEntity = userRepo.findById(id).get();
		teamservice.incrementTeamSize(newgid);
		userEntity.setTeamId(newgid);
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully added Team id for user id- " + id);

		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.TEAM_USER_ADDED);
		event.setFromUser(user.getName());
		event.setToUser(userEntity.getUname());
		event.setUserId(user.getId().toString());
		event.setDescription(
				user.getName() + " added user :" + userEntity.getUname() + " to Team with teamid :" + teamName);
		historyService.logEvent(event);
		return convertUserEntityToUserObject(userEntity);
	}

	public UserObject updateUserType(Integer id, String newusertype, UserObject user) {
		UserEntity userEntity = userRepo.findById(id).get();

		if (newusertype.equals(UserType.ADMIN.toString()))
			userEntity.setUserType(UserType.ADMIN);
		else if (newusertype.equals(UserType.MANAGER.toString()))
			userEntity.setUserType(UserType.MANAGER);
		else
			userEntity.setUserType(UserType.NORMAL);
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully updated userType  user id- " + id);

		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.USER_PRIVILEGE_UPDATED);
		event.setFromUser(user.getName());
		event.setToUser(userEntity.getUname());
		event.setUserId(user.getId().toString());
		event.setDescription(user.getName() + " updated user : " + userEntity.getUname() + " to : " + newusertype);
		historyService.logEvent(event);
		return convertUserEntityToUserObject(userEntity);
	}

	public UserObject updateIsActive(Integer id, Integer newIsactive, UserObject user) {
		UserEntity userEntity = userRepo.findById(id).get();
		if (newIsactive.equals(0)) {
			userEntity.setIsActive(false);
			userEntity.setDeletedAt(new Date());
			userEntity.setTeamId(null);
		} else {
			userEntity.setIsActive(true);
			userEntity.setDeletedAt(null);
		}
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully updated user isactive(user deleted )   user id- " + id);

		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.USER_DELETION);
		event.setFromUser(user.getName());
		event.setToUser(userEntity.getUname());
		event.setUserId(user.getId().toString());
		event.setDescription(user.getName() + " Deleted user : " + userEntity.getUname() + " from hawkeye ");
		historyService.logEvent(event);

		return convertUserEntityToUserObject(userEntity);
	}

	public UserObject removeTeamOfUser(Integer uid, UserObject user, String teamName, boolean shouldSaveEvent) {
		UserEntity userEntity = userRepo.findById(uid).get();
		teamservice.decrementTeamSize(userEntity.getTeamId());
		userEntity.setTeamId(null);
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully removed user from team user id- " + uid);
		if (shouldSaveEvent) {
			AuditEvent event = new AuditEvent();
			event.setAction(EventAction.TEAM_USER_DELETED);
			event.setFromUser(user.getName());
			event.setToUser(teamName);
			event.setUserId(user.getId().toString());
			event.setDescription(user.getName() + " Deleted user :" + userEntity.getUname() + " from " + teamName);
			historyService.logEvent(event);
		}
		return convertUserEntityToUserObject(userEntity);
	}

	public UserObject changeUname(UserObject user, String uname) {
		UserEntity userEntity = userRepo.findById(user.getId()).get();
		userEntity.setUname(uname);
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully change user Name for user- " + userEntity.getUid());
		return convertUserEntityToUserObject(userEntity);
	}

	@SneakyThrows
	public UserObject changePassword(UserObject user, String oldPassword, String newPassword) {
		String oldPassPhrase = null;
		String newPassPhrase = null;
		try {
			oldPassPhrase = CryptoHelp.generateStorngPasswordHash(oldPassword, key16Char);
			newPassPhrase = CryptoHelp.generateStorngPasswordHash(newPassword, key16Char);
		} catch (Exception e) {
			throw new GenericRuntimeException("Some Issue with Password.");
		}
		UserEntity userEntity = userRepo.findByEmailAndPassword(user.getEmail(), oldPassPhrase);
		if (userEntity == null) {
			throw new ObjectNotFoundException("User Not Found.");
		}
		userEntity.setPassword(newPassPhrase);
		userRepo.saveAndFlush(userEntity);
		log.debug(CLASS_NAME + " Successfully change user Name for user- " + userEntity.getUid());
		return convertUserEntityToUserObject(userEntity);
	}

	@SneakyThrows
	public List<UserObject> findAllUserForManager(UserObject userObject) {
		List<UserObject> userObjectList = new ArrayList<>();
		if (userObject.getUserType().equals(UserType.ADMIN.toString()))
			return findAllUser();
		if (userObject.getUserType().equals(UserType.MANAGER.toString())) {
			List<UserEntity> userList = userRepo.findByTeamId(userObject.getTeamId());
			userObjectList = new ArrayList<UserObject>();
			if (userList == null || userList.size() == 0)
				throw new ObjectNotFoundException();
			for (UserEntity userEntity : userList) {
				userObjectList.add(convertUserEntityToUserObject(userEntity));
			}
			return userObjectList;
		} else {
			return userObjectList;
		}
	}

	@SneakyThrows
	public List<UserObject> findAllUserwithTeamidNull() {
		List<UserEntity> userList = userRepo.findByTeamIdAndIsActive(null, true);
		List<UserObject> userObjectList = new ArrayList<UserObject>();
		if (userList == null || userList.size() == 0)
			throw new ObjectNotFoundException();
		for (UserEntity userEntity : userList) {
			userObjectList.add(convertUserEntityToUserObject(userEntity));
		}
		return userObjectList;
	}

	/**
	 * Utility method convert Entity to DTO
	 * 
	 * @param userEntity
	 * @return
	 */
	public UserObject convertUserEntityToUserObject(UserEntity userEntity) {
		UserObject userToSent = new UserObject();
		userToSent.setEmail(userEntity.getEmail());
		userToSent.setName(userEntity.getUname());
		userToSent.setId(userEntity.getUid());
		userToSent.setUserType(userEntity.getUserType().toString());
		userToSent.setTeamId(userEntity.getTeamId());
		userToSent.setCreated(userEntity.getCreatedOn());
		return userToSent;
	}

}
