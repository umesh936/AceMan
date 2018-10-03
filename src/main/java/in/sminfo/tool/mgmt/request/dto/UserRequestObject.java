package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class UserRequestObject {
	private String name;
	private String email;
	private String userType;
	private String password;
	private Integer teamId;

}
