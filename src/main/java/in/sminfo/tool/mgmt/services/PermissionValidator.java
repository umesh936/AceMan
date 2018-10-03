package in.sminfo.tool.mgmt.services;

import org.springframework.stereotype.Service;

@Service
public class PermissionValidator {

	public boolean checkIfUserHavePermisisonForResource(Integer userId, String resource) {
		return true;
	}
}
