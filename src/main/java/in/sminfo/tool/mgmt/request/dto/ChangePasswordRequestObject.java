package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class ChangePasswordRequestObject {
	String oldPassword;
	String newPassword;
}
