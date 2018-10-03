package in.sminfo.tool.mgmt.resposne.dto;

import java.util.List;

import lombok.Data;

@Data
public class SshKeyDetailObject {
	private String userid;
	private String userName;
	private List<AccessObject> acclist;
}
