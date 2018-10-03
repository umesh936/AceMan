package in.sminfo.tool.mgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.VpcHOP;

public interface VpcHopRepository extends JpaRepository<VpcHOP, Integer> {

	VpcHOP findByIpAndAwsAccountId(String ip, String awsAccountId);

	VpcHOP findByVpcId(String vpcId);
}
