package in.sminfo.tool.mgmt.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.whitelistedVpc;

public interface WhitelistVpcRepository extends JpaRepository<whitelistedVpc, Integer> {

	List<whitelistedVpc> findByAwsAccountId(Integer accountId);

	void deleteByVpcId(String vpcId);

	whitelistedVpc findByVpcId(String vpcId);
	
	
	
	
}