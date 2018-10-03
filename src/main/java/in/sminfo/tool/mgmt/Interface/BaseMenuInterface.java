package in.sminfo.tool.mgmt.Interface;

import java.util.List;

import in.sminfo.tool.mgmt.enums.UserType;
import in.sminfo.tool.mgmt.menu.BaseMenuObject;

/**
 * Any new plugin which need to be added in left bar should implement this.
 * 
 * @author umesh
 *
 */
public interface BaseMenuInterface {
	/**
	 * This map should return the Menu DisplaName and Its url
	 * 
	 * @param userType - UserType
	 * @return List of BaseMenuObject
	 */
	public List<BaseMenuObject> getMenuUserForType(UserType userType);
}
