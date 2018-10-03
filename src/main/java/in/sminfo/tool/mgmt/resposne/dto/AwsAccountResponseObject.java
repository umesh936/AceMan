package in.sminfo.tool.mgmt.resposne.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AwsAccountResponseObject {
	private Integer id;
	private String logicalName;
	private String addedBy;
	private String state;
	private Date date;
	private Boolean isDefault;
}
