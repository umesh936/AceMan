package in.sminfo.tool.mgmt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.KeyPair;

public interface KeyPairRepo extends JpaRepository<KeyPair, Integer> {
	public List<KeyPair> findByAwsAccountId(Integer awsAccountId);
	public KeyPair findByAwsAccountIdAndKeypairName(Integer awsAccountId, String keypairName);
}
