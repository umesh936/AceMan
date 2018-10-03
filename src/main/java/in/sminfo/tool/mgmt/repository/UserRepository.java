package in.sminfo.tool.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.UserEntity;
import in.sminfo.tool.mgmt.enums.UserType;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	UserEntity findByEmailAndPassword(String email, String password);
	
	UserEntity findByEmailAndGoogleUid(String email, String googleUid);
	
	UserEntity findByUnameAndPassword(String uname, String password);

	UserEntity findByUname(String uname);
	
	List<UserEntity> findByIsActive(Boolean isActive);
	
	List<UserEntity> findByUnameIn(List<String> uname);
	
	List<UserEntity> findByTeamIdAndIsActive(Integer teamId,Boolean isActive);
	
	List<UserEntity> findByUserTypeAndIsActiveAndTeamId(UserType usertype,Boolean isActive,Integer teamId);

	List<UserEntity> findByTeamId(Integer teamId);
	
	
	
	
	
}