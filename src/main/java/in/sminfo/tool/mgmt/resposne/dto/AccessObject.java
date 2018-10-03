package in.sminfo.tool.mgmt.resposne.dto;

import java.util.List;

import lombok.Data;

@Data
public class AccessObject {
	private String id;
	private List<String> ips;
	private String accountId;
}
