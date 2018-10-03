package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class SshKeyRequestObject {
	private String logicalName;
	private String key;
}
