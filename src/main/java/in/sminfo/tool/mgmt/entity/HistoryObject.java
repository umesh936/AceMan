package in.sminfo.tool.mgmt.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "audit_log")
@Data
public class HistoryObject implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = -6234527469219007757L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "from_user", nullable = false)
	private String fromUser;
	@Column(name = "access_type", nullable = false)
	private String accessType;
	@Column(name = "to_user", nullable = false)
	private String toUser;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "event_date")
	private Date eventDate;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "timestamp")
	private Date timestamp;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "is_success", nullable = false)
	private Boolean isSuccess;
}
