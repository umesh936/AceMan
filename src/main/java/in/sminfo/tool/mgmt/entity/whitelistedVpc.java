package in.sminfo.tool.mgmt.entity;

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
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "whitelist_vpc")
@Data
public class whitelistedVpc implements Serializable {

	private static final long serialVersionUID = 8175012406055396450L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "aws_account_id", nullable = false)
	private Integer awsAccountId;

	@Column(name = "vpc_id", nullable = false)
	private String vpcId;
	@Column(name = "vpc_name", nullable = false)
	private String vpcName;

	@Column(name = "added_by", nullable = false)
	private String addedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "added_on", nullable = false)
	private Date addedOn;

}
