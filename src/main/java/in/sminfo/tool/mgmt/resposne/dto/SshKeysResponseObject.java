package in.sminfo.tool.mgmt.resposne.dto;

import lombok.Data;

@Data
public class SshKeysResponseObject {
	private Integer id;
	private String name;
	private String sshKey;
}
