package in.sminfo.tool.mgmt.plugin.awsImpl;

import java.util.ArrayList;
import java.util.List;

import in.sminfo.tool.mgmt.Interface.BaseMenuInterface;
import in.sminfo.tool.mgmt.enums.UserType;
import in.sminfo.tool.mgmt.menu.BaseMenuObject;

public class AwsMenuList implements BaseMenuInterface {
	List<BaseMenuObject> menuList = new ArrayList<>();

	@Override
	public List<BaseMenuObject> getMenuUserForType(UserType userType) {
		switch (userType) {
		case NORMAL:
			menuList.add(new BaseMenuObject("List User", "javascript:void(0);", 1));
			break;
		case MANAGER:
			menuList.add(new BaseMenuObject("Create User", "javascript:void(0);", 1));
			menuList.add(new BaseMenuObject("Update User", "javascript:void(0);", 2));
			menuList.add(new BaseMenuObject("List User", "javascript:void(0);", 3));
			break;
		case ADMIN:
			menuList.add(new BaseMenuObject("Configure Account", "javascript:void(0);", 1));
			menuList.add(new BaseMenuObject("Create User", "javascript:void(0);", 1));
			menuList.add(new BaseMenuObject("Update User", "javascript:void(0);", 2));
			menuList.add(new BaseMenuObject("List User", "javascript:void(0);", 3));
			break;
		default:
			break;
		}
		menuList.sort((BaseMenuObject o1, BaseMenuObject o2) -> o2.getOrdinalPosition() - o1.getOrdinalPosition());
		return menuList;
	}

}
