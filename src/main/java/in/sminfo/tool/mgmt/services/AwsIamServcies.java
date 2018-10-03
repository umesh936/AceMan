package in.sminfo.tool.mgmt.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.entity.AwsAccount;
import in.sminfo.tool.mgmt.entity.KeyPair;
import in.sminfo.tool.mgmt.repository.AwsAccountRepo;
import in.sminfo.tool.mgmt.repository.KeyPairRepo;
import in.sminfo.tool.mgmt.repository.UserRepository;
import in.sminfo.tool.mgmt.resposne.dto.KeyPairResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.AwsRequestOverrideConfig;
import software.amazon.awssdk.core.auth.AwsCredentials;
import software.amazon.awssdk.core.auth.AwsCredentialsProvider;
import software.amazon.awssdk.core.auth.StaticCredentialsProvider;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.iam.IAMClient;
import software.amazon.awssdk.services.iam.model.CreateUserRequest;
import software.amazon.awssdk.services.iam.model.CreateUserResponse;
import software.amazon.awssdk.services.iam.model.ListUsersRequest;
import software.amazon.awssdk.services.iam.model.ListUsersResponse;
import software.amazon.awssdk.services.iam.model.UpdateAccountPasswordPolicyRequest;
import software.amazon.awssdk.services.iam.model.UpdateAccountPasswordPolicyResponse;
import software.amazon.awssdk.services.iam.model.User;

@Service
@Slf4j
public class AwsIamServcies {
	private final String CLASS_NAME = this.getClass().getName();
	@Resource
	UserRepository userRepo;
	@Resource
	KeyPairRepo keyPairRepo;
	@Resource
	AwsAccountRepo awsAccountRepo;

	Region region = Region.AWS_GLOBAL;
	IAMClient iam = IAMClient.builder().region(region).build();

	public List<UserObject> getUserList(Integer accountId) {
		List<User> awsUserList = new ArrayList<>();
		boolean done = false;
		String new_marker = null;
		AwsAccount account = awsAccountRepo.findById(accountId).get();
		AwsCredentials credentials = AwsCredentials.create(account.getAccessKey(), account.getSecretKey());
		AwsCredentialsProvider cp = StaticCredentialsProvider.create(credentials);
		AwsRequestOverrideConfig acf = AwsRequestOverrideConfig.builder().credentialsProvider(cp).build();
		while (!done) {
			ListUsersResponse response;

			if (new_marker == null) {
				ListUsersRequest request = ListUsersRequest.builder().requestOverrideConfig(acf).build();
				response = iam.listUsers(request);
				log.debug(CLASS_NAME + " - response from IAM : " + response);
			} else {
				ListUsersRequest request = ListUsersRequest.builder().requestOverrideConfig(acf).marker(new_marker).build();
				response = iam.listUsers(request);
			}

			for (User user : response.users()) {
				//System.out.format("Retrieved user %s", user.userName());
				awsUserList.add(user);
			}

			if (!response.isTruncated()) {
				done = true;
			} else {
				new_marker = response.marker();
			}

		}
		AwsIamServcies.log.debug(CLASS_NAME + "AWS User List Size: " + awsUserList.size());
		List<UserObject> test = new ArrayList<>();
		for (User user : awsUserList) {
			UserObject userObject = new UserObject(user.userName());
			userObject.setCreated(new Date(user.createDate().getEpochSecond()));
			test.add(userObject);
		}

		return test;
	}

	public void CreateUser(String username, String password) {
		Region region = Region.AWS_GLOBAL;
		IAMClient iam = IAMClient.builder().region(region).build();
		CreateUserRequest request = CreateUserRequest.builder().userName(username).build();

		CreateUserResponse response = iam.createUser(request);
		log.debug(CLASS_NAME + "Successfully created user: " + response.user().userName());
		UpdateAccountPasswordPolicyRequest uprequest = UpdateAccountPasswordPolicyRequest.builder()
				.allowUsersToChangePassword(true).build();
		UpdateAccountPasswordPolicyResponse uamResonse = iam.updateAccountPasswordPolicy(uprequest);

		// // ChangePasswordRequest requestPass =
		// //
		// ChangePasswordRequest.builder().oldPassword("").newPassword(password)
		// // .build();
		// // ChangePasswordResponse responsePass =
		// // iam.changePassword(requestPass);
		// System.out.println("Successfully created user password: ");
	}

	// public static void main(String[] args) {
	// CreateUser("test_to_delete_3", "test1");
	// }

	public List<KeyPairResponseObject> getKeyPairListForAccount(Integer accountId) {
		List<KeyPair> keyPairList = keyPairRepo.findByAwsAccountId(accountId);
		List<KeyPairResponseObject> listTosend = new ArrayList<>();
		for (KeyPair keyPair : keyPairList) {
			KeyPairResponseObject object = new KeyPairResponseObject();
			object.setId(keyPair.getId().toString());
			object.setKeyPairName(keyPair.getKeypairName());
			listTosend.add(object);
		}
		return listTosend;
	}
}
