package in.sminfo.tool.mgmt.request.dto;

import lombok.Data;

@Data
public class AwsAccountRequestObject {
	private String name;
	private String accessKey;
	private String secretKey;
}
