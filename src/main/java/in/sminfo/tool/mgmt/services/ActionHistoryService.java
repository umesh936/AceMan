package in.sminfo.tool.mgmt.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.entity.HistoryObject;
import in.sminfo.tool.mgmt.entity.Incident;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import in.sminfo.tool.mgmt.repository.HistoryRepository;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.resposne.dto.VpcObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.ec2.model.Vpc;

/**
 * Class is used to saving for History logs.
 * 
 * @author umesh
 *
 */
@Service
@Slf4j
public class ActionHistoryService {
	@Resource
	private HistoryRepository historyRepo;
	
	private HistoryObject historyobject;

	private static final String CLASS_NAME = "ActionHistoryService";
		
	public Integer logEvent(AuditEvent event) {
		HistoryObject eventHistory = new HistoryObject();
		eventHistory.setTimestamp(new Date());
		eventHistory.setAccessType(event.getAction().toString());
		eventHistory.setDescription(event.getDescription());
		eventHistory.setFromUser(event.getFromUser());
		eventHistory.setToUser(event.getToUser());
		eventHistory.setEventDate(new Date());
		eventHistory = historyRepo.saveAndFlush(eventHistory);
		log.debug("Event Logged Saved successFully for Action of user." + event.getFromUser());
		return eventHistory.getId();
	}

	@SneakyThrows
	@Async
	public void updateEventStatus(Integer eventid, Boolean result) {
		Optional<HistoryObject> optionObject = historyRepo.findById(eventid);
		HistoryObject eventHistory = new HistoryObject();
		if (optionObject.isPresent())
			optionObject.get();
		else
			throw new ObjectNotFoundException("Event Not found in Database.");
		eventHistory.setIsSuccess(result);
		eventHistory.setTimestamp(new Date());
		historyRepo.saveAndFlush(eventHistory);
		log.debug("Event updated Saved successFully for eventId." + eventid);
	}
//lastest written
	public List<AuditEvent> getAuditEvents(int page,UserObject user) {
		
		log.debug(CLASS_NAME+"getAuditEvents: called by user " +user.getName()+" "+user.getEmail());
		List<AuditEvent> auditEventslist = new  ArrayList<>();
		List<HistoryObject> historyObjectlist = historyRepo.findAllByOrderByTimestampDesc(new PageRequest(page, 10));
		
				System.out.println(auditEventslist);
		
		for (HistoryObject ho : historyObjectlist) {
			AuditEvent b = convertFromHistoryObjectToAuditEvent(ho);
			auditEventslist.add(b);
			
		}

		return auditEventslist;
	}
	
	
	private AuditEvent convertFromHistoryObjectToAuditEvent(HistoryObject history ) {
		
		AuditEvent object = new AuditEvent();
		EventAction action = EventAction.valueOf(history.getAccessType());
		object.setUserId(null);
		object.setAction(action);
		object.setDescription(history.getDescription());
		object.setEventDate(history.getTimestamp());
		object.setFromUser(history.getFromUser());
		object.setToUser(history.getToUser());

		return object;
		
	}

}
