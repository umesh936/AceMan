package in.sminfo.tool.mgmt.common.utilities;

import java.util.Date;

import in.sminfo.tool.mgmt.enums.EventAction;
import lombok.Data;

@Data
public class AuditEvent {
	private String userId;
	private EventAction action;
	private String description;
	private String fromUser;
	private String toUser;
	private Date eventDate;
}
