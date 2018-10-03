package in.sminfo.tool.mgmt.resposne.dto;

import in.sminfo.tool.mgmt.common.utilities.BasicGerritResponse;
import lombok.Data;

@Data
public class GerritResponseObject {
	private String id;
	private String name;
	private String email;
	private String username;
	private Boolean isActive;

	public GerritResponseObject(BasicGerritResponse response) {
		this.id = response.get_account_id();
		this.name = response.getName();
		this.email = response.getEmail();
		this.username = response.getUsername();
		this.isActive = false;
	}
}
