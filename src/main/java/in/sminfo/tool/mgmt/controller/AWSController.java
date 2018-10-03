package in.sminfo.tool.mgmt.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.entity.AwsAccount;
import in.sminfo.tool.mgmt.enums.UserType;
import in.sminfo.tool.mgmt.exception.InvalidRequestException;
import in.sminfo.tool.mgmt.exception.ObjectNotFoundException;
import in.sminfo.tool.mgmt.repository.AwsAccountRepo;
import in.sminfo.tool.mgmt.request.dto.AwsAccountRequestObject;
import in.sminfo.tool.mgmt.resposne.dto.AwsAccountResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.ELBObject;
import in.sminfo.tool.mgmt.resposne.dto.InstanceObject;
import in.sminfo.tool.mgmt.resposne.dto.KeyPairResponseObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.resposne.dto.VpcObject;
import in.sminfo.tool.mgmt.services.AwsAccountService;
import in.sminfo.tool.mgmt.services.AwsEc2Services;
import in.sminfo.tool.mgmt.services.AwsIamServcies;
import in.sminfo.tool.mgmt.services.PermissionValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/aws")
public class AWSController {
	private final String CLASS_NAME = this.getClass().getName();

	@Resource
	PermissionValidator permissionValidator;
	@Resource
	AwsIamServcies awsIamService;
	@Resource
	AwsEc2Services ec2Service;
	@Resource
	AwsAccountRepo awsAccountRepo;
	@Resource
	AwsAccountService awsAccountService;

	@PostMapping("/users")
	public void createUser() {

	}

	@SneakyThrows
	@GetMapping("/desc/users")
	public ResponseEntity<?> ListUser(HttpServletRequest request) {
		log.info(CLASS_NAME + ": List Aws Users Request received");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<UserObject>>(awsIamService.getUserList(awsId), HttpStatus.OK);
	}

	@SneakyThrows
	@PostMapping("/accounts")
	public void SaveAccount(@RequestBody AwsAccountRequestObject awsRequest, HttpServletRequest request,
			HttpServletResponse response) {
		log.info(CLASS_NAME + ": Save accounts Request received");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		if (!UserType.canCreateAwsAccount(user.getUserType()))
			throw new InvalidRequestException("Operation Not allowed.");
		Integer awsId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		awsAccountService.saveAccount(awsId, awsRequest, user);
	}

	@SneakyThrows
	@PostMapping("/accounts/default/{id}")
	public void makeAwsAccountActive(@PathVariable("id") Integer id, HttpServletRequest request,
			HttpServletResponse response) {
		log.info(CLASS_NAME + ": Making Aws Account active - " + id);
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		if (!UserType.canCreateAwsAccount(user.getUserType()))
			throw new InvalidRequestException("Operation Not allowed.");
		AwsAccount account = awsAccountRepo.findById(id).isPresent() ? awsAccountRepo.findById(id).get() : null;
		if (account == null)
			throw new ObjectNotFoundException("Account Id not found");
		session.setAttribute(Constant.SESSION.AWS_ACCOUNT_ID, id);
		session.setAttribute(Constant.SESSION.AWS_ACCOUNT_NAME, awsAccountRepo.findById(1).get().getLogicalName());
	}

	@SneakyThrows
	@GetMapping("/desc/accounts")
	public ResponseEntity<?> ListAccount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		Integer awsId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<AwsAccountResponseObject>>(awsAccountService.getListOfAws(awsId), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/desc/accounts/keypair")
	public ResponseEntity<?> ListKeyPairForAccount(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		Integer accountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<KeyPairResponseObject>>(awsIamService.getKeyPairListForAccount(accountId),
				HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/desc/ec2")
	public ResponseEntity<?> GetEc2Details(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		if (user.getUserType().equals(UserType.ADMIN.name())) {
			System.out.println("in admin ec2 funcition");
			return new ResponseEntity<Map<String, List>>(ec2Service.listEc2(awsAccountId, false), HttpStatus.OK);

		} else {
			System.out.println("in Manager and normal user  ec2 funcition");
			return new ResponseEntity<Map<String, List>>(ec2Service.listEc2(awsAccountId, true), HttpStatus.OK);

		}
	}

	@SneakyThrows
	@GetMapping("/desc/autoscale")
	public ResponseEntity<?> GetAWSDetails(HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();

		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<String>>(ec2Service.descAutoscale(awsAccountId), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/desc/autoscale/{name}")
	public ResponseEntity<?> getEc2listforGroup(@PathVariable("name") String gname, HttpServletRequest request,
			HttpServletResponse response) {

		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<InstanceObject>>(ec2Service.listEc2ForAutoscalingGroup(gname, awsAccountId),
				HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/desc/vpc")
	public ResponseEntity<?> GetRegionsDetails(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		if (user.getUserType().equals(UserType.ADMIN.name())) {
			return new ResponseEntity<List<VpcObject>>(ec2Service.descVpc(awsAccountId), HttpStatus.OK);
		} else {
			return new ResponseEntity<List<VpcObject>>(ec2Service.getWhitelistedVpc(awsAccountId), HttpStatus.OK);
		}
	}

	@SneakyThrows
	@GetMapping("/desc/vpc/{vpcId}/{region}")
	public ResponseEntity<?> GetEc2ListForVpc(@PathVariable("vpcId") String vpcId,
			@PathVariable("region") String region, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<InstanceObject>>(ec2Service.listEc2ForVpc(vpcId, awsAccountId, region),
				HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/desc/elb")
	public ResponseEntity<?> GetElbsDetails(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<ELBObject>>(ec2Service.listLoadBalancer(awsAccountId), HttpStatus.OK);
	}

	@SneakyThrows
	@GetMapping("/desc/elb/{name}/{region}")
	public ResponseEntity<?> GetElbByName(@PathVariable("name") String name, @PathVariable("region") String region,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		return new ResponseEntity<List<InstanceObject>>(ec2Service.getLoadBalancerDetail(name, awsAccountId, region),
				HttpStatus.OK);
	}

	@SneakyThrows
	@DeleteMapping(value = { "/whitelistVpc/{vpcId}" })
	public void whitelistVpc(@PathVariable("vpcId") String vpcId, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession(false);

		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		log.debug(CLASS_NAME + ": whitelistVpc - " + user.getName() + " triggered to delete Vpc " + vpcId);

		ec2Service.deleteVpcdata(awsAccountId, vpcId, user);
	}

	@SneakyThrows
	@PostMapping("/whitelistVpc/{vpcId}/{vpcName}")
	public void SaveWhitelistedvpc(@PathVariable("vpcId") String vpcId, @PathVariable("vpcName") String vpcName,
			HttpServletRequest request, HttpServletResponse response) {
		log.info(CLASS_NAME + ": Save accounts Request received");
		HttpSession session = request.getSession(false);
		if (session == null)
			throw new InvalidRequestException();
		UserObject user = (UserObject) session.getAttribute(Constant.SESSION.USER);
		if (user == null)
			throw new InvalidRequestException();
		if (!UserType.canCreateAwsAccount(user.getUserType()))
			throw new InvalidRequestException("Operation Not allowed.");
		Integer awsAccountId = (Integer) session.getAttribute(Constant.SESSION.AWS_ACCOUNT_ID);
		ec2Service.saveWhiteListVpc(awsAccountId, vpcId, vpcName, user);
	}

	@GetMapping("/users/{name}")
	public void GetUserDetails() {

	}

	@GetMapping("/desc/envs")
	public void GetEnvDetails() {

	}
}
