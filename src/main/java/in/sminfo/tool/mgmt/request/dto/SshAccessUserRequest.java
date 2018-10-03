package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class SshAccessUserRequest {
	private String keyPairName;
	private String instanceId;
	private String instanceIP;
	private String groupName;
	private String vpcId;
	private Integer noOfDays;
	private String region;
	
}
