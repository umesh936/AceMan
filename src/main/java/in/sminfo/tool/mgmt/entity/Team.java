package in.sminfo.tool.mgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.sminfo.tool.mgmt.enums.UserType;
import lombok.Data;

@Entity
@Table(name = "team")
@Data
public class Team implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4861929680313685469L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "team_name", nullable = false)
	private String teamName;
	@Column(name = "manager_uid", nullable = false)
	private Integer managerUid;
	@Column(name = "created_by", nullable = false)
	private String createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	Date createdOn;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "team_size", nullable = false)
	private Integer teamSize;

}
