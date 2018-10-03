package in.sminfo.tool.mgmt.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ssh_keys")
@Data
public class SshKeys implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9137516021520207841L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "logical_name", nullable = false)
	private String name;
	@Column(name = "ssh_key", nullable = false)
	private String sshKey;
	@Column(name = "is_active", nullable = false)
	private Boolean isActive;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uid")
	private UserEntity user;
}
