package in.sminfo.tool.mgmt.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.entity.Team;
import in.sminfo.tool.mgmt.exception.InvalidRequestException;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import in.sminfo.tool.mgmt.repository.TeamRepo;
import in.sminfo.tool.mgmt.request.dto.TeamObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.services.SshService;
import in.sminfo.tool.mgmt.services.TeamService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/teams")
public class TeamController {
	private static final String CLASS_NAME = "TeamController";
	@Resource
	TeamRepo teamRepo;
	@Resource
	TeamService teamService;
	@Resource
	SshService sshService;

	@SneakyThrows
	@PostMapping("")
	public ResponseEntity<Integer> saveTeam(@RequestBody TeamObject teamRequestObj, HttpServletRequest request,
			HttpServletResponse response) {
		log.info(CLASS_NAME + ": Save Team Request received");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		
		
		return new ResponseEntity<Integer>(teamService.saveNewTeam(teamRequestObj, user), HttpStatus.OK);
	}

	@GetMapping("/desc")
	public ResponseEntity<?> listTeamName() {
		return new ResponseEntity<List<Team>>(teamRepo.findAll(), HttpStatus.OK);
	}

	@GetMapping("/manager/{managerUid}")
	public ResponseEntity<?> ListTeamNameByManagerId(@PathVariable("managerUid") Integer managerUid) {
		return new ResponseEntity<List<Team>>(teamRepo.findByManagerUid(managerUid), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/name/{teamName}")
	public ResponseEntity<?> getTeamDetailByName(@PathVariable("teamName") String teamName) {
		Team team = teamRepo.findByTeamName(teamName);
		if (team == null)
			throw new ObjectNotFoundException();
		return new ResponseEntity<Team>(team, HttpStatus.OK);
	}

	@GetMapping("/{teamId}/users")
	public ResponseEntity<?> getTeamDetail(@PathVariable("teamId") Integer teamId) {
		return new ResponseEntity<List<UserObject>>(teamService.getAllUserForTeam(teamId), HttpStatus.OK);
	}

	@SneakyThrows
	@DeleteMapping(value = { "/delete/{teamid}/{teamName}" })
	public void deleteTeam(@PathVariable("teamid") Integer teamId, @PathVariable("teamName") String teamName,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		log.info(CLASS_NAME + ": deleteTeam request receved for teamid - " + teamId);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();

		log.debug(CLASS_NAME + ": deleteTeam - " + user.getName() + " triggered to delete team " + teamName);
		teamService.deleteTeam(teamId, teamName, user);
	}

	// @GetMapping("/test/1")
	// public void getTeamDetail1(@PathParam("teamId") String teamId) {
	// sshService.authenticateSSH("35.154.213.127", 1);
	// }
}
