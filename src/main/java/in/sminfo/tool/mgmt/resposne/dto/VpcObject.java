package in.sminfo.tool.mgmt.resposne.dto;

import lombok.Data;

@Data
public class VpcObject {
	String vpcName;
	String vpcId;
	Boolean isWhitelist;
	String region;
}
