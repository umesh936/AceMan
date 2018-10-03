package in.sminfo.tool.mgmt.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.entity.Team;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.exception.GenericRuntimeException;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import in.sminfo.tool.mgmt.repository.TeamRepo;
import in.sminfo.tool.mgmt.request.dto.TeamObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TeamService {
	private static final String CLASS_NAME = "TeamService";
	@Resource
	UIUserService userService;
	@Resource
	ActionHistoryService historyService;
	@Resource
	TeamRepo teamRepo;

	public Integer saveNewTeam(TeamObject teamRequestObj, UserObject user) {

		Team newTeam = new Team();
		newTeam.setCreatedBy(teamRequestObj.getCreatedBy());
		newTeam.setCreatedOn(new Date());
		newTeam.setDescription(teamRequestObj.getDescription());
		newTeam.setManagerUid(teamRequestObj.getManagerUid());
		newTeam.setTeamName(teamRequestObj.getTeamName());
		newTeam.setTeamSize(0);
		newTeam = teamRepo.saveAndFlush(newTeam);

		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.TEAM_CREATION);
		event.setUserId(String.valueOf(user.getId()));
		event.setDescription(user.getName() + "- created Team With name " + teamRequestObj.getTeamName());
		historyService.logEvent(event);
		return newTeam.getId();
	}

	@SneakyThrows
	public boolean AddUserInTeam(Integer teamId, String userIdToAdd, String userIdOperating) {
		if (!userService.canUserUpdateTeam(Integer.parseInt(userIdOperating)))
			throw new GenericRuntimeException("Operation not allowed.");
		Optional<Team> mentionedTeam = teamRepo.findById(teamId);
		if (!mentionedTeam.isPresent()) {
			throw new ObjectNotFoundException("Team Id not found in database.");
		}
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.TEAM_USER_ADDED);
		event.setFromUser(userIdOperating);
		event.setToUser(userIdToAdd);
		event.setUserId(userIdOperating);
		event.setDescription(userIdOperating + "- added user" + userIdToAdd + " in team " + teamId);
		historyService.logEvent(event);
		return true;
	}

	@SneakyThrows
	public List<UserObject> getAllUserForTeam(Integer teamId) {
		return userService.findAllUserByTeamId(teamId);
	}

	@SneakyThrows
	public void deleteTeam(Integer teamId, UserObject user) {
		log.info(CLASS_NAME + " Delete Team Request for -" + teamId + " by user :" + user.getName());
		Optional<Team> mentionedTeam = teamRepo.findById(teamId);
		if (!mentionedTeam.isPresent()) {
			throw new ObjectNotFoundException("Team Id not found in database.");
		}

	}

	@SneakyThrows
	public void deleteTeam(Integer teamId, String teamName, UserObject user) {

		List<UserObject> teamUserlist = userService.findAllUserByTeamId(teamId);

		if (!teamUserlist.isEmpty()) {
			for (UserObject userobject : teamUserlist) {
				System.out.println("deleting user -" + userobject.getName());
				userService.removeTeamOfUser(userobject.getId(), user, teamName, false);
			}
		}
		teamRepo.deleteById(teamId);
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.TEAM_DELETION);
		event.setFromUser(user.getName());
		event.setToUser(teamName);
		event.setUserId(user.getId().toString());
		event.setDescription(user.getName() + "- Deleted Team " + teamName);
		historyService.logEvent(event);
	}

	@SneakyThrows
	public void incrementTeamSize(Integer teamId) {
		Optional<Team> mentionedTeam = teamRepo.findById(teamId);

		if (!mentionedTeam.isPresent()) {
			throw new ObjectNotFoundException("Team Id not found in database.");
		} else {
			Team var = mentionedTeam.get();
			var.setTeamSize(var.getTeamSize() == null ? 0 : var.getTeamSize() + 1);

		}

	}

	@SneakyThrows
	public void decrementTeamSize(Integer teamId) {
		Optional<Team> mentionedTeam = teamRepo.findById(teamId);

		if (!mentionedTeam.isPresent()) {
			throw new ObjectNotFoundException("Team Id not found in database.");
		} else {
			Team var = mentionedTeam.get();
			var.setTeamSize(var.getTeamSize() - 1);

		}

	}

}
