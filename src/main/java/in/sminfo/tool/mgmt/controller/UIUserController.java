package in.sminfo.tool.mgmt.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.enums.UserType;
import in.sminfo.tool.mgmt.exception.InvalidRequestException;
import in.sminfo.tool.mgmt.request.dto.ChangePasswordRequestObject;
import in.sminfo.tool.mgmt.request.dto.UserRequestObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.services.ActionHistoryService;
import in.sminfo.tool.mgmt.services.UIUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/ui-users")
public class UIUserController {
	private static final String CLASS_NAME = "UIUserController :";
	@Resource
	UIUserService uiUserService;

	@SneakyThrows
	@PostMapping("/")
	public void createUser(@RequestBody UserRequestObject userObject, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		uiUserService.register(userObject);
	}

	@SneakyThrows
	@GetMapping("/desc")
	public ResponseEntity<?> listUser(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		if (user.getUserType().equals(UserType.ADMIN.name())) {
			return new ResponseEntity<List<UserObject>>(uiUserService.findAllActiveUser(), HttpStatus.OK);
		} else if (user.getUserType().equals(UserType.MANAGER.name())) {
			return new ResponseEntity<List<UserObject>>(uiUserService.findAllUserByTeamId(user.getTeamId()),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("{\"msg\":\"You are not authorised to access this.\"", HttpStatus.OK);
		}

	}

	@SneakyThrows
	@GetMapping("/managers")
	public ResponseEntity<?> listManager(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();

		if (user.getUserType().equals(UserType.ADMIN.name())) {
			return new ResponseEntity<List<UserObject>>(uiUserService.findAllManagers(UserType.MANAGER), HttpStatus.OK);

		} else if (user.getUserType().equals(UserType.MANAGER.name())) {

			List<UserObject> userlist = new ArrayList<UserObject>();
			userlist.add(user);
			return new ResponseEntity<List<UserObject>>(userlist, HttpStatus.OK);
		} else {

			return new ResponseEntity<String>("{\"msg\":\"You are not authorised to access this.\"", HttpStatus.OK);
		}

	}

	@SneakyThrows
	@GetMapping("/teamid/notassociated")
	public ResponseEntity<?> fetchUserWhoDontHaveTeamId(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();

		return new ResponseEntity<List<UserObject>>(uiUserService.findAllUserwithTeamidNull(), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserDetailsById(@PathVariable("id") Integer Id, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<UserObject>(uiUserService.findUserById(Id), HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/users/pwd")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequestObject changePassword,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<UserObject>(
				uiUserService.changePassword(user, changePassword.getOldPassword(), changePassword.getNewPassword()),
				HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/update/usertype")
	public ResponseEntity<?> updateUserType(@RequestBody Map<String, String> userdata, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		String newusertype = userdata.get("usertype");
		Integer id1 = Integer.valueOf(userdata.get("id"));

		if (user.getId().equals(id1))
			throw new InvalidRequestException();

		return new ResponseEntity<UserObject>(uiUserService.updateUserType(id1, newusertype, user), HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/update/isactive")
	public ResponseEntity<?> updateUserisactive(@RequestBody Map<String, String> userdata, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		Integer newIsactive = Integer.valueOf(userdata.get("IsActive"));
		Integer id1 = Integer.valueOf(userdata.get("id"));

		if (user.getId().equals(id1))
			throw new InvalidRequestException();

		return new ResponseEntity<UserObject>(uiUserService.updateIsActive(id1, newIsactive, user), HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/users/email/{email}")
	public ResponseEntity<?> changeEmail(@PathVariable("email") String newEmail, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<UserObject>(uiUserService.changeEmail(user, newEmail), HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/users/delteam/{userId}/{teamName}")
	public ResponseEntity<?> removeTeamOfUser(@PathVariable("userId") Integer uid,
			@PathVariable("teamName") String teamName, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<UserObject>(uiUserService.removeTeamOfUser(uid, user, teamName, true), HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/users/update/{gid}/{uid}/{teamName}")
	public ResponseEntity<?> addTeamIdofUser(@PathVariable("gid") Integer newgid, @PathVariable("uid") Integer uid,
			@PathVariable("teamName") String teamName, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("in addteamIdtoUser function");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<UserObject>(uiUserService.addTeamId(uid, newgid, teamName, user), HttpStatus.OK);
	}

	@SneakyThrows
	@PutMapping("/users/uname/{uname}")
	public ResponseEntity<?> changeUname(@PathVariable("uname") String uname, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		return new ResponseEntity<UserObject>(uiUserService.changeUname(user, uname), HttpStatus.OK);
	}

}
