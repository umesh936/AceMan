package in.sminfo.tool.mgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "aws_account")
@Data
public class AwsAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7980189298869513282L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "access_key", nullable = false)
	private String accessKey;
	@Column(name = "secret_key", nullable = false)
	private String secretKey;
	@Column(name = "logical_name", nullable = false)
	private String logicalName;
	@Column(name = "added_by", nullable = false)
	private String addedBy;
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "added_on", nullable = false)
	private Date date;
//	@OneToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "id")
	@Column(name = "uid", nullable = false)
	private Integer userid;
}
