package in.sminfo.tool.mgmt.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ssh_access_detail")
@Data
public class SshAccessRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9137516021520207841L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "uid", nullable = false)
	private Integer uid;
	@Column(name = "aws_account_id", nullable = false)
	private Integer awsAccountId;
	@Column(name = "instance_id", nullable = false)
	private String instanceId;
	@Column(name = "instance_ip", nullable = false)
	private String instanceIP;
	@Column(name = "vpc_id", nullable = false)
	private String vpcId;
	@Column(name = "created_on")
	private Date eventDate;
	@Column(name = "expiry_date")
	private Date expiryDate;
	@Column(name = "is_expired")
	private Boolean isExpired;
	@Column(name = "given_by", nullable = false)
	private Integer givenByUser;

}
