package in.sminfo.tool.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.SshKeys;
import in.sminfo.tool.mgmt.entity.UserEntity;

public interface SshUserRepo extends JpaRepository<SshKeys, Integer> {
	public List<SshKeys> findByUser(UserEntity user);
	
	public List<SshKeys> findByIsActive(Boolean isActive);

	public SshKeys findByUserAndIsActive(UserEntity user, Boolean isActive);
	public SshKeys findByName(String name);
}
