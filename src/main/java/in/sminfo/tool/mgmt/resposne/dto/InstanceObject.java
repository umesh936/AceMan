package in.sminfo.tool.mgmt.resposne.dto;

import java.time.Instant;
import java.util.Map;

import lombok.Data;

@Data
public class InstanceObject {
	private String instanceId;
	private String keyName;
	private String instanceType;
	private String publicIpAddress;
	private String privateIpAddress;
	private String vpcId;
	private String subnetId;
	private String state;
	private String name;
	private String envi;
	private Map<String, String> tags;
	private Instant createdAt;
	private String region;
}
