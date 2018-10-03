package in.sminfo.tool.mgmt.scheduler;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import in.sminfo.tool.mgmt.entity.SshAccessRecord;
import in.sminfo.tool.mgmt.repository.SshAccessRecordRepo;
import in.sminfo.tool.mgmt.services.SshService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CheckDailyStatus {
	private static final String CLASS_NAME = "CheckDailyStatus";

	@Resource
	SshAccessRecordRepo sshAccessRecordRepo;
	@Resource
	SshService sshService;

	@Scheduled(cron = "0 1 1 * * ?")
	public void updatePrivilidge() throws Exception {
		log.info(CLASS_NAME + "scheduler start ");
		List<SshAccessRecord> listRecord = sshAccessRecordRepo.findByExpiryDate(new Date());
		for (SshAccessRecord sshAccessRecord : listRecord) {
			sshService.deleteAccess(sshAccessRecord,"Scheduler");
		}
	}

}
