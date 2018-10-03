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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "vpc_hop")
@Data
public class VpcHOP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1800438302074035179L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "aws_account_id", nullable = false)
	private Integer awsAccountId;

	@Column(name = "vpc_id", nullable = false)
	private String vpcId;

	@Column(name = "added_by", nullable = false)
	private String addedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "added_on", nullable = false)
	private Date addedOn;

	@Column(name = "ip", nullable = false)
	private String ip;

	@Column(name = "url_path", nullable = false)
	private String url;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "keypair_id")
	private KeyPair keypair;

}
