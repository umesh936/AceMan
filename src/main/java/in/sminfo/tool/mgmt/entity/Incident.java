package in.sminfo.tool.mgmt.entity;

import org.apache.tomcat.jni.Time;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "incident", type = "incident", shards = 1, replicas = 0, refreshInterval = "-1")

public class Incident {
	@Id
	private String  incidentId;
	private String  incidentName;
	private String  incidentTime;
	private String  incidentCategory;
	private Boolean  isprocessed;
	private String  reason;
	private String  rectification;
	private String  rectifiedPerson;
	private String  ServiceAttached;
	private String  incidentDowntime;
	private String  reportedBy;
	private String  reportedTime;
	
	public Incident() {
		
	}
	
	public Incident(String incidentId, String incidentName, String incidentTime, String incidentCategory,
			Boolean isprocessed, String reason, String rectification, String rectifiedPerson, String serviceAttached,
			String incidentDowntime, String reportedBy, String reportedTime) {
		
		super();
		this.incidentId = incidentId;
		this.incidentName = incidentName;
		this.incidentTime = incidentTime;
		this.incidentCategory = incidentCategory;
		this.isprocessed = isprocessed;
		this.reason = reason;
		this.rectification = rectification;
		this.rectifiedPerson = rectifiedPerson;
		ServiceAttached = serviceAttached;
		this.incidentDowntime = incidentDowntime;
		this.reportedBy = reportedBy;
		this.reportedTime = reportedTime;
	}
	public String getIncidentId() {
		return incidentId;
	}
	public void setIncidentId(String incidentId) {
		this.incidentId = incidentId;
	}
	public String getIncidentName() {
		return incidentName;
	}
	public void setIncidentName(String incidentName) {
		this.incidentName = incidentName;
	}
	public String getIncidentTime() {
		return incidentTime;
	}
	public void setIncidentTime(String incidentTime) {
		this.incidentTime = incidentTime;
	}
	public String getIncidentCategory() {
		return incidentCategory;
	}
	public void setIncidentCategory(String incidentCategory) {
		this.incidentCategory = incidentCategory;
	}
	public Boolean getIsprocessed() {
		return isprocessed;
	}
	public void setIsprocessed(Boolean isprocessed) {
		this.isprocessed = isprocessed;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRectification() {
		return rectification;
	}
	public void setRectification(String rectification) {
		this.rectification = rectification;
	}
	public String getRectifiedPerson() {
		return rectifiedPerson;
	}
	public void setRectifiedPerson(String rectifiedPerson) {
		this.rectifiedPerson = rectifiedPerson;
	}
	public String getServiceAttached() {
		return ServiceAttached;
	}
	public void setServiceAttached(String serviceAttached) {
		ServiceAttached = serviceAttached;
	}
	public String getIncidentDowntime() {
		return incidentDowntime;
	}
	public void setIncidentDowntime(String incidentDowntime) {
		this.incidentDowntime = incidentDowntime;
	}
	public String getReportedBy() {
		return reportedBy;
	}
	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}
	public String getReportedTime() {
		return reportedTime;
	}
	public void setReportedTime(String reportedTime) {
		this.reportedTime = reportedTime;
	}
	
	

}
