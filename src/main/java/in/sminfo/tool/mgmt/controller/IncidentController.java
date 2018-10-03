package in.sminfo.tool.mgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.entity.Incident;
import in.sminfo.tool.mgmt.services.IncidentService;


@RestController
public class IncidentController {
	
	@Autowired // for dependency injection of courseService int this controller 
	private IncidentService customerService;
	
	
	//all the part of GET request
	@RequestMapping("/incident/{page}")
	public List<Incident> getAllUsers(@PathVariable("page") int page) {
		return customerService.getUnprocessedIncidents(false,page);
	}
	
	//get users by name
//	@RequestMapping("/incident/{name}")
//	public List<Incident> getUsersByName(@PathVariable("name") String username) {
//		return customerService.getIncidentByincidentName(username);
//	}
//	@RequestMapping("/incident/{isprocessed}")
//	public List<Incident> getUnprocessedIncidents(@PathVariable("isprocessed") Boolean isprocessed) {
//		return customerService.getUnprocessedIncidents(isprocessed,page);
//	}
	
	// POST Request  
	@RequestMapping(method=RequestMethod.POST, value="/incident")// this will be invoked when there is a post request in topics
	public void addIncident(@RequestBody Incident incident) { //@RequestBody converts the json which is in url to Topic instance
		customerService.addIncident(incident);
	}
	
	
//	// POST Request  
//		@RequestMapping(method=RequestMethod.PUT, value="/incident")// this will be invoked when there is a post request in topics
//		public void addTopic(@RequestBody IncidentDTO incident) { //@RequestBody converts the json which is in url to Topic instance
//			customerService.updateCustomer(incident);
//		}
}
