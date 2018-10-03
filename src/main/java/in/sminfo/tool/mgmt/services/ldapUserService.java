package in.sminfo.tool.mgmt.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sminfo.tool.mgmt.entity.ldapUser;
import in.sminfo.tool.mgmt.repository.ldapUserRepository;

@Service
public class ldapUserService {

	@Autowired
	private ldapUserRepository ldapuserRepository;

	public List<ldapUser> getUsersByName(String username) {
		List<ldapUser> users = new ArrayList<>();
		users = ldapuserRepository.getPersonNamesByName(username);
		System.out.println(users);
		return users;

	}
	
	public List<ldapUser> getAllUsers() {
		List<ldapUser> users = new ArrayList<>();
		users = ldapuserRepository.getAllUsers();
		System.out.println(users);
		return users;

	}

}
