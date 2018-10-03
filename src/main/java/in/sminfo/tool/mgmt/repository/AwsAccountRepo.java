package in.sminfo.tool.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.AwsAccount;


public interface AwsAccountRepo extends JpaRepository<AwsAccount, Integer> {

	

}
