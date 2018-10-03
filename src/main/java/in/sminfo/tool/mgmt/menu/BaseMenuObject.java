package in.sminfo.tool.mgmt.menu;

public class BaseMenuObject {

	private String menuName;
	private String menuLink;
	private Integer ordinalPosition;

	public BaseMenuObject(String menuName, String menuLink, Integer ordinalPosition) {
		super();
		this.menuName = menuName;
		this.menuLink = menuLink;
		this.ordinalPosition = ordinalPosition;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuLink() {
		return menuLink;
	}

	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}

	public Integer getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(Integer ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

}
