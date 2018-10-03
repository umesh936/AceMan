package in.sminfo.tool.mgmt.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.entity.Incident;
import in.sminfo.tool.mgmt.repository.IncidentRepository;

@Service
public class IncidentService {

	@Autowired
	private IncidentRepository increpository;

	public List<Incident> fetchAllIncidents() {
		List<Incident> incident = new ArrayList<>();
		Iterator<Incident> customerItr = increpository.findAll().iterator();
		System.out.println(" Fteched Data");
		System.out.println(customerItr.toString());

		while (customerItr.hasNext()) {
			Incident a = (Incident) customerItr.next();
			incident.add(a);
			System.out.println("next->" + customerItr.hasNext());

		}
		return incident;

	}

	public List<Incident> getUnprocessedIncidents(Boolean isactive,int page) {
		List<Incident> events = new ArrayList<>();
		events = (List<Incident>) increpository.findByIsprocessed(isactive, new PageRequest(page, 10));
		System.out.println("======\n"+events);
		return events;

	}
	public List<Incident> getIncidentByincidentName(String username) {
		List<Incident> events = new ArrayList<>();
		events = (List<Incident>) increpository.findByIncidentName(username);
		System.out.println(events);
		return events;

	}

	public void addIncident(Incident incident) {
		increpository.save(incident);

	}

//	public void addCustomer(IncidentDTO incidentdto) {
//		Incident incident = increpository.findById(id);
//		incident.setre
//		increpository.save(incident);
//
//	}

	
}
