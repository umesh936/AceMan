package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class RemoteSshRequestDTO {
	String ip;
	String instanceId;
	String accountId;
	String keyPairFileName;
	String keyPairUserName;
	String groupName;
	String sshUserName;

}
