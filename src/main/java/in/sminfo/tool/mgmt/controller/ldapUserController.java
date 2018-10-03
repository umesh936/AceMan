package in.sminfo.tool.mgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.sminfo.tool.mgmt.entity.ldapUser;
import in.sminfo.tool.mgmt.services.ldapUserService;

@RestController	
public class ldapUserController {
	@Autowired // for dependency injection of courseService int this controller 
	private ldapUserService ldapuserService;
	
	//all the part of GET request
	@GetMapping("/ldap/accounts")
	public List<ldapUser> getAllUsers() {
		return ldapuserService.getAllUsers();
	}
	
	//get users by name
	@GetMapping("/ldap/accounts/{cn}")
	public List<ldapUser> getUsersByName(@PathVariable("cn") String username) {
		return ldapuserService.getUsersByName(username);
	}
}
