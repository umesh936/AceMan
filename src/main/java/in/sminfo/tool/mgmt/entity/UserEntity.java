package in.sminfo.tool.mgmt.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import in.sminfo.tool.mgmt.enums.UserType;
import lombok.Data;

@Entity
@Table(name = "user" + "")
@Data
public class UserEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8175012406055396450L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "uid", unique = true, nullable = false)
	private Integer uid;
	@Column(name = "uname", nullable = false)
	private String uname;
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "email", nullable = false)
	private String email;
	@Column(name = "team_id", nullable = false)
	private Integer teamId;
	@Enumerated(EnumType.STRING)
	@Column(name = "user_type", nullable = false)
	private UserType userType;
	@Column(name = "google_uid", nullable = false)
	private String googleUid;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_on")
	Date createdOn;
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
	private List<SshKeys> sshUsers;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at")
	Date deletedAt;


}
