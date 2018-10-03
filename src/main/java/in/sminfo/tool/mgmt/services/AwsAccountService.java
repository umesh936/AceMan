package in.sminfo.tool.mgmt.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.entity.AwsAccount;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.repository.AwsAccountRepo;
import in.sminfo.tool.mgmt.repository.UserRepository;
import in.sminfo.tool.mgmt.request.dto.AwsAccountRequestObject;
import in.sminfo.tool.mgmt.resposne.dto.AwsAccountResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AwsAccountService {
	private final String CLASS_NAME = this.getClass().getName();
	@Resource
	AwsAccountRepo awsAccountRepo;
	@Resource
	UserRepository userRepo;
	@Resource
	ActionHistoryService historyService;
	@Resource
	VaultService vaultService;

	public void saveAccount(Integer awsId, AwsAccountRequestObject request, UserObject user) {
		log.debug(CLASS_NAME + "- Account creation Request");
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.AWS_ACCOUNT_CREATION);
		event.setUserId(user.getName());
		event.setDescription(user.getName() + "- created AWS Account With name." + request.getName());
		Integer Id = historyService.logEvent(event);

		AwsAccount account = new AwsAccount();
		vaultService.writeAwsAccountAccessKeys(awsId, request.getAccessKey(), request.getSecretKey());
		account.setLogicalName(request.getName());
		account.setAddedBy(user.getName());
		account.setIsActive(true);
		account.setDate(new Date());
		awsAccountRepo.saveAndFlush(account);
		historyService.updateEventStatus(Id, true);
	}

	public List<AwsAccountResponseObject> getListOfAws(Integer awsId) {
		List<AwsAccount> awsAccountList = awsAccountRepo.findAll();
		List<AwsAccountResponseObject> listTosend = new ArrayList<>();
		for (AwsAccount awsAccount : awsAccountList) {
			AwsAccountResponseObject object = new AwsAccountResponseObject();
			object.setId(awsAccount.getId());
			object.setLogicalName(awsAccount.getLogicalName());
			object.setAddedBy(awsAccount.getAddedBy());
			object.setState(awsAccount.getIsActive() ? "Active" : "DeActive");
			// TODO: No option on UI to make it Deactive For a while.
			if (awsAccount.getId() == awsId)
				object.setIsDefault(true);
			else
				object.setIsDefault(false);
			object.setDate(new Date(awsAccount.getDate().getTime()));
			listTosend.add(object);
		}
		return listTosend;
	}

}
