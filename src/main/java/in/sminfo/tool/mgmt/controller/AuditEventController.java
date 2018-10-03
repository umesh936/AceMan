package in.sminfo.tool.mgmt.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.exception.InvalidRequestException;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.services.ActionHistoryService;
import lombok.SneakyThrows;




@RestController
public class AuditEventController {
	
	@Autowired // for dependency injection of courseService int this controller 
	private ActionHistoryService auditEventservice;
	
	//all the part of GET request
	@SneakyThrows
	@RequestMapping("/auditEvent/desc/{page}")
	public List<AuditEvent> getAllLogs(@PathVariable("page") int page,  HttpServletRequest request,
			HttpServletResponse response) {
			HttpSession session = request.getSession(false);
			if (session == null)
				throw new InvalidRequestException();
			UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
			if (user == null)
				throw new InvalidRequestException();
		return auditEventservice.getAuditEvents(page,user);
	}
	
	
}
