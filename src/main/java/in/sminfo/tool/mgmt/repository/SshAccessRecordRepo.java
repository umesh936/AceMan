package in.sminfo.tool.mgmt.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.entity.SshAccessRecord;

public interface SshAccessRecordRepo extends JpaRepository<SshAccessRecord, Integer> {

	public List<SshAccessRecord> findByUidAndAwsAccountIdAndIsExpired(Integer uid, Integer awsAccountId,
			Boolean isExpired);

	public List<SshAccessRecord> findByInstanceIdAndAwsAccountIdAndIsExpired(String instanceId, Integer awsAccountId,
			Boolean isExpired);

	public List<SshAccessRecord> findByInstanceIdAndUidAndIsExpired(String instanceId, Integer uid, Boolean isExpired);

	public SshAccessRecord findByInstanceIdAndUidAndAwsAccountIdAndIsExpired(String instanceId, Integer uid,
			Integer awsAccountId, Boolean isExpired);

	public List<SshAccessRecord> findByUidAndIsExpired(Integer uid, Boolean isExpired);

	public List<SshAccessRecord> findByInstanceIdAndIsExpired(String instanceId, Boolean isExpired);

	public List<SshAccessRecord> findByExpiryDate(Date expiryDate);

}
