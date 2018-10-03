package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class TeamObject {
	private String teamName;
	private Integer managerUid;
	private String createdBy;
	private String description;
	private Integer teamSize;
}
