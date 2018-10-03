package in.sminfo.tool.mgmt.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClient;
import com.amazonaws.services.autoscaling.AmazonAutoScalingClientBuilder;
import com.amazonaws.services.autoscaling.model.AutoScalingGroup;
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsResult;

import in.sminfo.tool.mgmt.common.utilities.AuditEvent;
import in.sminfo.tool.mgmt.entity.AwsAccount;
import in.sminfo.tool.mgmt.entity.whitelistedVpc;
import in.sminfo.tool.mgmt.enums.EventAction;
import in.sminfo.tool.mgmt.repository.AwsAccountRepo;
import in.sminfo.tool.mgmt.repository.WhitelistVpcRepository;
import in.sminfo.tool.mgmt.resposne.dto.ELBObject;
import in.sminfo.tool.mgmt.resposne.dto.InstanceObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.resposne.dto.VpcObject;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.auth.AwsCredentials;
import software.amazon.awssdk.core.auth.AwsCredentialsProvider;
import software.amazon.awssdk.core.auth.StaticCredentialsProvider;
import software.amazon.awssdk.core.regions.Region;
import software.amazon.awssdk.services.ec2.EC2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.DescribeVpcsResponse;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.Tag;
import software.amazon.awssdk.services.ec2.model.Vpc;
import software.amazon.awssdk.services.elasticloadbalancing.ElasticLoadBalancingClient;
import software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancersRequest;
import software.amazon.awssdk.services.elasticloadbalancing.model.DescribeLoadBalancersResponse;
import software.amazon.awssdk.services.elasticloadbalancing.model.LoadBalancerDescription;

@Slf4j
@Service
@Transactional
public class AwsEc2Services {
	private final String CLASS_NAME = this.getClass().getName();

	@Value("${aws.region.support}")
	String regionCsv;

	@Resource
	AwsAccountRepo accountRepo;
	@Resource
	WhitelistVpcRepository whitelistRepo;

	@Resource
	ActionHistoryService historyService;

	@Resource
	VaultService vaultService;
	// Small hack for autoscale group region
	private static Map<String, String> autoscaleMap = new HashMap<String, String>();

	private static Map<Integer, AwsCredentialsProvider> credentialMap = new HashMap<>();

	public List<VpcObject> descVpc(Integer accountId) {
		List<VpcObject> vpcList = new ArrayList<>();
		List<whitelistedVpc> whiteVpclist = whitelistRepo.findByAwsAccountId(accountId);
		log.debug(CLASS_NAME + ": descVpc -  for account " + accountId);
		String[] regionList = regionCsv.split(",");
		for (String region : regionList) {
			EC2Client ec2 = EC2Client.builder().region(Region.of(region))
					.credentialsProvider(getCredentialProvider(accountId)).build();
			DescribeVpcsResponse response = ec2.describeVpcs();
			for (Vpc vpc : response.vpcs()) {
				VpcObject vpcObj = new VpcObject();
				vpcObj.setVpcId(vpc.vpcId());
				if (containsVpc(whiteVpclist, vpc.vpcId())) {
					vpcObj.setIsWhitelist(true);
				} else {
					vpcObj.setIsWhitelist(false);
				}
				vpcObj.setVpcName(getNameFromTags(vpc.tags()));
				vpcObj.setRegion(region.toString());
				vpcList.add(vpcObj);
			}
		}

		return vpcList;
	}

	// --------------------autoscaling--------

	public List<String> descAutoscale(Integer accountId) {
		List<String> autoScaleNameList = new ArrayList<String>();
		String[] regionList = regionCsv.split(",");

		log.debug(CLASS_NAME + ": descAutoscale - list of Autoscale group requested for  : " + accountId);
		for (String region : regionList) {
			AmazonAutoScalingClient ab = (AmazonAutoScalingClient) AmazonAutoScalingClientBuilder.standard()
					.withRegion(region).withCredentials(new AWSStaticCredentialsProvider(getCredentialV1(accountId)))
					.build();
			DescribeAutoScalingGroupsResult autoscale = ab.describeAutoScalingGroups();
			List<AutoScalingGroup> autoscaleGrouplist = autoscale.getAutoScalingGroups();
			for (AutoScalingGroup autoScalingGroup : autoscaleGrouplist) {
				autoScaleNameList.add(autoScalingGroup.getAutoScalingGroupName());
				autoscaleMap.put(autoScalingGroup.getAutoScalingGroupName() + "." + accountId, region);
			}
		}
		return autoScaleNameList;
	}

	/**
	 * 
	 * @param groupname
	 *            - AutoScale Groupname
	 *
	 * @param accountId
	 *            - aws AccountId
	 * @return
	 */
	public List<InstanceObject> listEc2ForAutoscalingGroup(String groupname, Integer accountId) {

		log.debug(CLASS_NAME + ": listEc2ForAutoscalingGroup - Seraching for AutoScale : " + groupname);
		String region = autoscaleMap.get(groupname + "." + accountId);
		// set default region as mumbai
		if (region == null)
			region = Regions.AP_SOUTH_1.name();
		AmazonAutoScalingClient ab = (AmazonAutoScalingClient) AmazonAutoScalingClientBuilder.standard()
				.withRegion(Regions.valueOf(region))
				.withCredentials(new AWSStaticCredentialsProvider(getCredentialV1(accountId))).build();
		DescribeAutoScalingGroupsResult autoscale = ab.describeAutoScalingGroups();

		List<AutoScalingGroup> AutoscaleGrouplist = autoscale.getAutoScalingGroups();

		List<com.amazonaws.services.autoscaling.model.Instance> instancelist = null;
		for (AutoScalingGroup autoScalingGroup : AutoscaleGrouplist) {
			if (autoScalingGroup.getAutoScalingGroupName().equals(groupname)) {
				log.debug(CLASS_NAME + "in listEc2ForAutoscalingGroup --- controller");
				instancelist = autoScalingGroup.getInstances();
				log.debug(CLASS_NAME + instancelist.size());
			}
		}
		List<String> instanceIdList = new ArrayList<String>();
		for (com.amazonaws.services.autoscaling.model.Instance ins : instancelist) {
			instanceIdList.add(ins.getInstanceId());
		}

		return describeEc2InstanceByIds(instanceIdList, accountId, region);
	}

	// -------------------------------
	/**
	 * Get all White listed Vpc
	 * 
	 * @param accountId
	 *            - account Id of aws
	 * @return list of {@link VpcObject}
	 */
	public List<VpcObject> getWhitelistedVpc(Integer accountId) {
		List<VpcObject> vpcList = new ArrayList<>();
		log.debug(CLASS_NAME + ": getWhitelistedVpc -  for account " + accountId);
		List<whitelistedVpc> whiteVpclist = whitelistRepo.findByAwsAccountId(accountId);
		for (whitelistedVpc vpc : whiteVpclist) {
			VpcObject vpcObj = new VpcObject();
			vpcObj.setVpcId(vpc.getVpcId());
			vpcObj.setIsWhitelist(true);
			vpcObj.setVpcName(vpc.getVpcName());
			vpcList.add(vpcObj);
		}
		return vpcList;
	}

	/**
	 * List all Ec2 using in Particular Account Id.
	 * 
	 * @param accountId
	 *            - account Id
	 * @return return Map
	 */
	public Map<String, List> listEc2(Integer accountId, boolean fromWhiteListed) {
		log.debug(CLASS_NAME + ":listEc2- For Account id " + accountId);
		List<InstanceObject> instanceList = new ArrayList<>();
		String[] regionList = regionCsv.split(",");
		for (String region : regionList) {
			EC2Client ec2 = EC2Client.builder().region(Region.of(region))
					.credentialsProvider(getCredentialProvider(accountId)).build();
			DescribeInstancesResponse response = ec2.describeInstances();
			instanceList.addAll(
					getInstanceListFromReservationList(response.reservations(), accountId, fromWhiteListed, region));
		}

		List<String> environemntlist = new ArrayList<>();
		for (InstanceObject instance : instanceList) {
			String value = instance.getEnvi();
			if (value != null && !value.trim().isEmpty() && !environemntlist.contains(value))
				environemntlist.add(value);
		}

		HashMap<String, List> ans = new HashMap<>();
		ans.put("environment", environemntlist);
		ans.put("instance", instanceList);
		return ans;
	}

	/**
	 * List Ec2 for particular VPC
	 * 
	 * @param vpcId
	 *            vpcId
	 * @param accountId
	 *            account Id
	 * @return list of {@link InstanceObject}
	 */
	public List<InstanceObject> listEc2ForVpc(String vpcId, Integer accountId, String region) {
		EC2Client ec2 = EC2Client.builder().region(Region.of(region))
				.credentialsProvider(getCredentialProvider(accountId)).build();
		List<InstanceObject> instanceList = new ArrayList<InstanceObject>();
		log.debug(CLASS_NAME + ": listEc2ForVpc - Seraching for VPC : " + vpcId);
		DescribeInstancesResponse response = ec2.describeInstances();
		for (Reservation reservation : response.reservations()) {
			for (Instance instance : reservation.instances()) {
				if (instance.vpcId() != null)
					if (instance.vpcId().equals(vpcId)) {
						instanceList.add(convertAwsInstanceToInstanceObject(instance, region));
					}
			}
		}
		return instanceList;
	}

	/**
	 * List all Load balancer.
	 * 
	 * @param accountId
	 *            account Id
	 * @return list of {@link ELBObject}
	 */
	public List<ELBObject> listLoadBalancer(Integer accountId) {
		List<ELBObject> listToSend = new ArrayList<>();
		String[] regionList = regionCsv.split(",");
		for (String region : regionList) {
			ElasticLoadBalancingClient elbClient = ElasticLoadBalancingClient.builder().region(Region.of(region))
					.credentialsProvider(getCredentialProvider(accountId)).build();
			DescribeLoadBalancersResponse response = elbClient.describeLoadBalancers();
			log.debug(CLASS_NAME + ": ListLoadBalancer - requested list of loadBalancers  ");
			for (LoadBalancerDescription description : response.loadBalancerDescriptions()) {
				ELBObject object = new ELBObject();
				object.setName(description.loadBalancerName());
				object.setVpcid(description.vpcId());
				object.setRegion(region);
				List<String> instanceIds = new ArrayList<>();
				for (software.amazon.awssdk.services.elasticloadbalancing.model.Instance instance : description
						.instances()) {
					instanceIds.add(instance.instanceId());
				}
				object.setInstanceIds(instanceIds);
				listToSend.add(object);
			}
		}
		log.debug(CLASS_NAME + ": ListLoadBalancer - ELB : " + listToSend);
		return listToSend;
	}

	/**
	 * Get Load Balancer Details for particular account Id.
	 * 
	 * @param name
	 *            name
	 * @param accountId
	 *            account Id
	 * @return list of {@link InstanceObject}
	 */
	public List<InstanceObject> getLoadBalancerDetail(String name, Integer accountId, String region) {
		List<String> instanceIdList = new ArrayList<>();
		ElasticLoadBalancingClient elbClient = ElasticLoadBalancingClient.builder().region(Region.of(region))
				.credentialsProvider(getCredentialProvider(accountId)).build();

		DescribeLoadBalancersRequest request = DescribeLoadBalancersRequest.builder().loadBalancerNames(name).build();
		DescribeLoadBalancersResponse response = elbClient.describeLoadBalancers(request);
		log.debug(CLASS_NAME + "getLoadBalancerDetail: REQUESTED loadbalancer detail of " + name);
		for (software.amazon.awssdk.services.elasticloadbalancing.model.Instance instance : response
				.loadBalancerDescriptions().get(0).instances()) {
			instanceIdList.add(instance.instanceId());
		}
		return describeEc2InstanceByIds(instanceIdList, accountId, region);
	}

	public void saveWhiteListVpc(Integer awsAccountId, String vpcId, String vpcName, UserObject user) {

		whitelistedVpc data = whitelistRepo.findByVpcId(vpcId);
		System.out.println(data);
		if (data == null) {

			whitelistedVpc vpcdata = new whitelistedVpc();
			vpcdata.setAwsAccountId(awsAccountId);
			vpcdata.setVpcId(vpcId);
			vpcdata.setAddedBy(user.getEmail());
			vpcdata.setAddedOn(new Date());
			vpcdata.setVpcName(vpcName);
			whitelistRepo.save(vpcdata);

			AuditEvent event = new AuditEvent();
			event.setAction(EventAction.VPC_WHITELISTED);
			event.setFromUser(null);
			event.setUserId(user.getId().toString());
			event.setDescription(user.getName() + "Whitelisted Vpc with VPCid - " + vpcId);
			log.debug(CLASS_NAME + "saveWhiteListVpc :  saved  in whitelistvpc table " + vpcId + ":" + vpcName);
			log.info(CLASS_NAME + "saveWhiteListVpc : " + vpcId + " marked as whitelisted ");

			historyService.logEvent(event);
		}
	}

	public void deleteVpcdata(Integer awsAccountId, String vpcId, UserObject user) {
		log.info(CLASS_NAME + "deleteVpcdata :  deleted from table in whitelist " + vpcId);

		whitelistRepo.deleteByVpcId(vpcId);
		AuditEvent event = new AuditEvent();
		event.setAction(EventAction.VPC_REMOVED_WHITELISTED);
		event.setFromUser(user.getName());
		event.setUserId(user.getId().toString());
		event.setDescription(user.getName() + " removed vpc from whitelisted with Vpcid - " + vpcId);
		log.info(CLASS_NAME + "deleteVpcdata :  deleted from table in whitelist " + vpcId);
		historyService.logEvent(event);

	}

	/**
	 * To describe the Ec2 with respect to instance Id.
	 * 
	 * @param instanceIds
	 *            list of Instance Ids
	 * @param accountId
	 *            account Id
	 * @return list of {@link InstanceObject}
	 */
	public List<InstanceObject> describeEc2InstanceByIds(List<String> instanceIds, Integer accountId, String region) {
		EC2Client ec2 = EC2Client.builder().region(Region.of(region))
				.credentialsProvider(getCredentialProvider(accountId)).build();
		DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder().instanceIds(instanceIds)
				.build();
		DescribeInstancesResponse response = ec2.describeInstances(describeInstancesRequest);
		return getInstanceListFromReservationList(response.reservations(), accountId, false, region);
	}

	private InstanceObject convertAwsInstanceToInstanceObject(Instance instance, String region) {
		InstanceObject object = new InstanceObject();
		object.setInstanceId(instance.instanceId());
		object.setName(getNameFromTags(instance.tags()));
		object.setInstanceType(instance.instanceTypeString());
		object.setState(instance.state().nameString());
		object.setVpcId(instance.vpcId());
		object.setSubnetId(instance.subnetId());
		object.setPrivateIpAddress(instance.privateIpAddress());
		object.setPublicIpAddress(instance.publicIpAddress());
		object.setKeyName(instance.keyName());
		object.setCreatedAt(instance.launchTime());
		object.setRegion(region);

		Map<String, String> tags = new HashMap<>();
		if (instance.tags() == null) {
			return object;
		}

		for (Tag tag : instance.tags()) {

			if (tag.key().equals("Environment")) {
				object.setEnvi(tag.value());
			} else if (tag.key().equals("Name"))
				object.setName(tag.value());

			else
				tags.put(tag.key(), tag.value());
		}
		object.setTags(tags);
		return object;
	}

	private List<InstanceObject> getInstanceListFromReservationList(List<Reservation> reservationList,
			Integer accountId, boolean isWhitelistedCheck, String region) {
		log.debug(CLASS_NAME + " Total machine : %s , account ID -  %s , isWhitelistedCheck : %s ",
				reservationList.size(), accountId, isWhitelistedCheck);

		List<InstanceObject> instanceList = new ArrayList<InstanceObject>();
		List<whitelistedVpc> whiteVpclist = null;
		if (isWhitelistedCheck) {
			whiteVpclist = whitelistRepo.findByAwsAccountId(accountId);
		}
		for (Reservation reservation : reservationList) {
			for (Instance instance : reservation.instances()) {
				if (whiteVpclist != null) {
					if (containsVpc(whiteVpclist, instance.vpcId()))
						instanceList.add(convertAwsInstanceToInstanceObject(instance, region));
				} else
					instanceList.add(convertAwsInstanceToInstanceObject(instance, region));
			}
		}
		return instanceList;

	}

	private String getNameFromTags(List<Tag> tagList) {
		if (tagList == null)
			return "N/A";
		for (Tag tag : tagList) {
			if (!tag.key().equals("Name"))
				continue;
			String name = tag.value();
			return name;
		}
		return "N/A";
	}

	/**
	 * This for aws old sdk version which is used in getting autoscale group
	 * information.
	 * 
	 * @param accountId
	 * @return
	 */
	private AWSCredentials getCredentialV1(Integer accountId) {
		AwsAccount account = vaultService.readAwsAccountAccessKeys(accountId);
		AWSCredentials credentials = new BasicAWSCredentials(account.getAccessKey(), account.getSecretKey());
		return credentials;
	}

	private AwsCredentialsProvider getCredentialProvider(Integer accountId) {
		AwsCredentialsProvider cp = credentialMap.get(accountId);
		if (cp == null) {
			AwsAccount account = vaultService.readAwsAccountAccessKeys(accountId);
			AwsCredentials credentials = AwsCredentials.create(account.getAccessKey(), account.getSecretKey());
			cp = StaticCredentialsProvider.create(credentials);
			credentialMap.put(1, cp);
		}
		return cp;
	}

	/**
	 * To check vpcId present in whitelistes VPCs or not
	 * 
	 * @param list
	 *            list of whitelist Vpc
	 * @param Vpcid
	 *            vpcId to check
	 * @return true / false
	 */
	private boolean containsVpc(final List<whitelistedVpc> list, final String Vpcid) {
		return list.stream().filter(o -> o.getVpcId().equals(Vpcid)).findFirst().isPresent();
	}

}
