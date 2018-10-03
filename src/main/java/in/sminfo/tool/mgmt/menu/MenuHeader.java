package in.sminfo.tool.mgmt.menu;

import java.util.List;

/**
 *  Responsible for holding the Menu Details.
 * @author umesh
 *
 */
public class MenuHeader {

	private String menuHeaderName;
	private List<BaseMenuObject> underLyingMenuList;

	public MenuHeader(String menuHeaderName, List<BaseMenuObject> underLyingMenuList) {
		super();
		this.menuHeaderName = menuHeaderName;
		this.underLyingMenuList = underLyingMenuList;
	}

	/**
	 * @return the menuHeaderName
	 */
	public String getMenuHeaderName() {
		return menuHeaderName;
	}

	/**
	 * @param menuHeaderName
	 *            the menuHeaderName to set
	 */
	public void setMenuHeaderName(String menuHeaderName) {
		this.menuHeaderName = menuHeaderName;
	}

	/**
	 * @return the underLyingMenuList
	 */
	public List<BaseMenuObject> getUnderLyingMenuList() {
		return underLyingMenuList;
	}

	/**
	 * @param underLyingMenuList
	 *            the underLyingMenuList to set
	 */
	public void setUnderLyingMenuList(List<BaseMenuObject> underLyingMenuList) {
		this.underLyingMenuList = underLyingMenuList;
	}

}
