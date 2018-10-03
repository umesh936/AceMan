package in.sminfo.tool.mgmt.resposne.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserObject {
	private Integer id;
	private String name;
	private String email;
	private String userType;
	private Integer teamId;
	private Integer sshId;
	private String env;
	private Date created;

	public UserObject(String name) {
		this.name = name;
	}
	public UserObject() {
	}
}
