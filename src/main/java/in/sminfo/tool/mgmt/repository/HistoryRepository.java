package in.sminfo.tool.mgmt.repository;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.entity.AwsAccount;
import in.sminfo.tool.mgmt.entity.HistoryObject;

public interface HistoryRepository extends JpaRepository<HistoryObject, Integer> {

	

	List<HistoryObject> findAllByOrderByTimestampDesc(Pageable pageable);
	

}
