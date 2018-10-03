package in.sminfo.tool.mgmt.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.common.utilities.BasicGerritResponse;
import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.common.utilities.CustomRestTemplate;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.resposne.dto.GerritResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.services.ActionHistoryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gerrit")
public class GerritController {

	@Value("${gerrit.url}")
	String gerritUrl;

	@Value("${gerrit.userName}")
	String userName;

	@Value("${gerrit.password}")
	String password;

	private final String CLASS_NAME = "GerritController";
	@Resource
	ActionHistoryService historyService;
	/**
	 * Get list of all Account.
	 * 
	 * @param model
	 *            - model Map
	 * @param name
	 *            - Name of Admin User
	 * @return List of Accounts {@link GerritResponseObject}
	 */
	@SneakyThrows
	@GetMapping(value = { "/accounts" })
	public ResponseEntity<List<GerritResponseObject>> getAllAccount(ModelMap model, @RequestParam("name") String name) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(gerritUrl + "/accounts/?q=" + name, String.class,
				headers);
		String resStr = response.getBody().substring(response.getBody().indexOf("["));
		List<BasicGerritResponse> gerritReponse = new ObjectMapper().readValue(resStr.trim(),
				new TypeReference<List<BasicGerritResponse>>() {
				});
		List<GerritResponseObject> listToSend = new ArrayList<>();
		for (BasicGerritResponse basicGerritResponse : gerritReponse) {
			GerritResponseObject object = new GerritResponseObject(basicGerritResponse);
			String result = getAccountState(object.getId());
			if (result != null && result.trim().equals("ok")) {
				object.setIsActive(true);
			} else {
				object.setIsActive(false);
			}
			listToSend.add(object);
		}
		return new ResponseEntity<List<GerritResponseObject>>(listToSend, HttpStatus.OK);

	}

	/**
	 * Account Id can be Id or email of user.
	 * 
	 * @param model
	 *            model map
	 * @param accountId
	 *            account Id
	 */

	@DeleteMapping(value = { "/accounts/{accountId}" })
	public void deleteAccountActiveState(ModelMap model, @PathVariable("accountId") String accountId,HttpServletRequest request,
			HttpServletResponse response) {

		log.info(CLASS_NAME + ":deactivate request has been requested for gerrit user");
		RestTemplate restTemplate = CustomRestTemplate.restTemplate(gerritUrl, 443, userName, password);
		ResponseEntity<String> gerritresponse = restTemplate.exchange(gerritUrl + "/a/accounts/" + accountId + "/active",
				HttpMethod.DELETE, null, String.class);
		HttpSession session = request.getSession(false);
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		log.debug(CLASS_NAME + ":deactivate user has been requested for gerritt user having account id"+accountId);
		log.debug(gerritresponse.getBody());
		
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.GERRIT_USER_DEACTIVATED);
		event.setFromUser(user.getName());
		event.setUserId(user.getId().toString());
		event.setDescription(user.getName() + " deactivated gerrit user  - "+ accountId);
		historyService.logEvent(event);

	}

	/**
	 * A account Id can be Id or email of user.
	 * 
	 * @param model
	 *            model map
	 * @param accountId
	 *            accountId
	 */
	@GetMapping(value = { "/accounts/{accountId}" })
	public void getAccountActiveState(ModelMap model, @PathVariable("accountId") String accountId) {
		getAccountState(accountId);

	}

	private String getAccountState(String accountId) {
		RestTemplate restTemplate = CustomRestTemplate.restTemplate(gerritUrl, 443, userName, password);
		ResponseEntity<String> response = restTemplate.getForEntity(gerritUrl + "/a/accounts/" + accountId + "/active",
				String.class);
		return response.getBody();
	}

	/**
	 * account Id can be Id or email of user.
	 * 
	 * @param model
	 *            model Map
	 * @param accountId
	 *            accountId
	 */
	@PutMapping(value = { "/accounts/{accountId}" })
	public void setAccountActiveState(ModelMap model, @PathVariable("accountId") String accountId,HttpServletRequest request,
			HttpServletResponse response) {

		log.info(CLASS_NAME + "Activate  has been requested for gerrit user"+accountId);
		RestTemplate restTemplate = CustomRestTemplate.restTemplate(gerritUrl, 443, userName, password);
		ResponseEntity<String> gerritresponse = restTemplate.exchange(gerritUrl + "/a/accounts/" + accountId + "/active",
				HttpMethod.PUT, null, String.class);
		
		
		HttpSession session = request.getSession(false);
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.GERRIT_USER_ACTIVATED);
		event.setFromUser(user.getName());
		event.setUserId(user.getId().toString());
		event.setDescription(user.getName() + " Activated gerrit user  - "+ accountId);
		historyService.logEvent(event);
		log.debug(gerritresponse.getBody());

	}

}
