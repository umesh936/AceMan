package in.sminfo.tool.mgmt.resposne.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ELBObject {
	private String name;
	private List<String> instanceIds;
	private String vpcid;
	private Date createdon;
	private String region;
}
