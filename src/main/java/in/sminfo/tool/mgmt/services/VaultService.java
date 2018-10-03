package in.sminfo.tool.mgmt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import in.sminfo.tool.mgmt.entity.AwsAccount;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VaultService {
	private static final String CLASS_NAME = "VaultService: ";

	@Autowired
	private VaultTemplate vaultTemplate;

	/**
	 * AWS account will be saved as
	 * 
	 * <pre>
	 * akey.accountId = access 
	 * Key skey.accountId = secret Key
	 * </pre>
	 * 
	 * @param accountId
	 */
	public AwsAccount readAwsAccountAccessKeys(int accountId) {
		VaultResponse response = vaultTemplate.read("secret/awsaccount/akey." + accountId);
		String accesskey = (String) response.getData().get("value");
		response = vaultTemplate.read("secret/awsaccount/skey." + accountId);
		String secretkey = (String) response.getData().get("value");
		AwsAccount awsAccount = new AwsAccount();
		awsAccount.setId(accountId);
		awsAccount.setAccessKey(accesskey);
		awsAccount.setSecretKey(secretkey);
		return awsAccount;
	}

	public void writeAwsAccountAccessKeys(int accountId, String accessKey, String secretKey) {
		vaultTemplate.write("secret/awsaccount/akey." + accountId, accessKey);
		vaultTemplate.write("secret/awsaccount/skey." + accountId, secretKey);
		log.debug(CLASS_NAME + "Wrote data to Vault for account Id " + accountId);
	}

	public String readAwsAccountKeysPair(int accountId, String keyPairName) {
		VaultResponse response = vaultTemplate.read("secret/awskey/" + keyPairName + "." + accountId);
		return (String) response.getData().get("value");
	}

	public void writeSSHKeys(String logicalName, String sshKeys) {
		vaultTemplate.write("secret/awsaccount/sshkey/" + logicalName, sshKeys);
		log.debug(CLASS_NAME + "Wrote ssh keys to Vault for username " + logicalName);
	}

	public String readSSHKeys(String logicalName) {
		VaultResponse response = vaultTemplate.read("secret/awsaccount/sshkey/" + logicalName);
		log.debug(CLASS_NAME + "read ssh keys to Vault for username " + logicalName);
		return (String) response.getData().get("value");
	}

}
