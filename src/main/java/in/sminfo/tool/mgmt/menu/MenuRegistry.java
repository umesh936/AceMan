package in.sminfo.tool.mgmt.menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to
 * 
 * @author umesh
 *
 */
public class MenuRegistry {
	private static ArrayList<MenuHeader> menuHeaderList = new ArrayList<>();

	public static void registerMenu(List<BaseMenuObject> menuList, String menuHeader) {
		menuHeaderList.add(new MenuHeader(menuHeader, menuList));
	}

	public List<MenuHeader> getMenuHeaderList() {
		return (List<MenuHeader>) menuHeaderList.clone();
	}

}
