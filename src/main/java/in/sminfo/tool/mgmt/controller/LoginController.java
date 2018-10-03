package in.sminfo.tool.mgmt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import in.sminfo.tool.mgmt.common.utilities.Constant;
import in.sminfo.tool.mgmt.common.utilities.StaticDataHolder;
import in.sminfo.tool.mgmt.repository.AwsAccountRepo;
import in.sminfo.tool.mgmt.repository.UserRepository;
import in.sminfo.tool.mgmt.resposne.dto.InstanceObject;
import in.sminfo.tool.mgmt.resposne.dto.UserObject;
import in.sminfo.tool.mgmt.services.AwsEc2Services;
import in.sminfo.tool.mgmt.services.UIUserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.util.EC2MetadataUtils;

@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {
	private static final String CLASS_NAME = "LoginController: ";

	@Resource
	UserRepository userRepo;
	@Resource
	UIUserService uiUserService;
	@Resource
	AwsEc2Services ec2Service;
	@Resource
	AwsAccountRepo awsAccountRepo;
	@Value("${aws.region.hawkeye}")
	String region;

	@Value("${google.app.CLIENT_ID}")
	String CLIENT_ID;

	/**
	 * On Application start redirect to login page
	 * 
	 * @param model
	 *            Model MAp
	 * @return return redirection
	 */
	@GetMapping(value = { "" })
	public String redirectToLoginPage(ModelMap model) {
		model.put("clienId", CLIENT_ID);
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView index(ModelMap model) {
		model.put("clienId", CLIENT_ID);
		return new ModelAndView("login");
	}

	@SneakyThrows
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest req, ModelMap model, @RequestParam("email") String email,
			@RequestParam("password") String password) {
		UserObject user = uiUserService.loginAllowed(email, password);
		log.info(CLASS_NAME + "Searching for email : " + email);
		if (user != null) {
			HttpSession session = req.getSession(true);
			session.setAttribute(Constant.SESSION.LOGGED_IN, true);
			session.setAttribute(Constant.SESSION.USER, user);
			session.setAttribute(Constant.SESSION.AWS_ACCOUNT_ID, 1);
			return new ModelAndView("redirect:/home");
		} else {
			model.put("clienId", CLIENT_ID);
			return new ModelAndView("redirect:/login?error=Invalid%20Credentials");
		}
	}

	@SneakyThrows
	@RequestMapping(value = "/google/tokensignin", method = RequestMethod.POST)
	public void loginViaGoogle(HttpServletRequest req, ModelMap model, @RequestParam("idtoken") String token,
			HttpServletRequest request, HttpServletResponse response) {
		log.debug(CLASS_NAME + "CLIENT_ID - '" + CLIENT_ID + "'");
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
				JacksonFactory.getDefaultInstance()).setAudience(Collections.singletonList(CLIENT_ID)).build();
		GoogleIdToken idToken = verifier.verify(token);
		if (idToken != null) {
			Payload payload = idToken.getPayload();
			// Print user identifier
			String userId = payload.getSubject();
			log.debug(CLASS_NAME + "User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			if (emailVerified) {
				UserObject userObject = uiUserService.googleLoginAllowed(email, userId, name);
				if (userObject != null) {
					HttpSession session = request.getSession(true);
					session.setAttribute(Constant.SESSION.LOGGED_IN, true);
					session.setAttribute(Constant.SESSION.USER, userObject);
					session.setAttribute(Constant.SESSION.AWS_ACCOUNT_ID, 1);
					session.setAttribute(Constant.SESSION.AWS_ACCOUNT_NAME,
							awsAccountRepo.findById(1).get().getLogicalName());
					response.setStatus(200);
					return;
				}
			}
		}
		response.setStatus(403);
		return;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home(HttpServletRequest req, ModelMap model) {
		if (req.getSession(false) == null) {
			log.info(CLASS_NAME + "Session not found! Redirecting User to Login Page.");
			return new ModelAndView("redirect:/login?error=Invalid%20SessionId");
		}
		UserObject user = (UserObject) req.getSession().getAttribute(Constant.SESSION.USER);
		model.addAttribute(Constant.MODEL.USER_NAME, user.getName());
		model.addAttribute(Constant.MODEL.USER_EMAIL, user.getEmail());
		model.addAttribute(Constant.MODEL.AWS_NAME, req.getSession().getAttribute(Constant.SESSION.AWS_ACCOUNT_NAME));
		String currentInstanceId = EC2MetadataUtils.getInstanceId();
		log.debug(CLASS_NAME + "home self instance Id : " + currentInstanceId);
		List<String> instanceIds = new ArrayList<String>();
		instanceIds.add(currentInstanceId);
		List<InstanceObject> list = ec2Service.describeEc2InstanceByIds(instanceIds, 1, region);
		StaticDataHolder.putData(Constant.DATA.SELF_VPC, list.get(0).getVpcId());
		StaticDataHolder.putData(Constant.DATA.SELF_INSTANCE_ID, currentInstanceId);
		log.info(CLASS_NAME + "User Found : " + user.getName());

		//
		return new ModelAndView("home", model);
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(ModelMap model, HttpServletRequest req) {
		req.getSession().invalidate();
		model.put("clienId", CLIENT_ID);
		return new ModelAndView("logout");
	}

}
