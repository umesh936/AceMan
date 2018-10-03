package in.sminfo.tool.mgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "key_pair")
@Data
public class KeyPair implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4959190635469267484L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "keypair_name", nullable = false)
	private String keypairName;
	@Column(name = "key", nullable = false)
	private String key;
	@Column(name = "aws_account_id", nullable = false)
	private Integer awsAccountId;
	@Column(name = "user_name", nullable = false)
	private String userName;
}
