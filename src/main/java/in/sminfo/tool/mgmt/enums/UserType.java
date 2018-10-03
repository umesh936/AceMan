package in.sminfo.tool.mgmt.enums;

/**
 * Enum to store the User type can access this application.
 * 
 * @author umesh
 *
 */
public enum UserType {
	/**
	 * Admin User
	 */
	ADMIN("ADMIN"),
	/**
	 * Team Leader User
	 */
	MANAGER("MANAGER"),
	/**
	 * Normal User
	 */
	NORMAL("NORMAL");

	private String _type;

	UserType(String type) {
		this._type = type;
	}

	@Override
	public String toString() {
		return this._type;
	}

	public static boolean canCreateUser(UserType type) {
		if (type == ADMIN || type == MANAGER)
			return true;
		return false;
	}

	public static boolean canCreateAwsAccount(UserType type) {
		if (type == ADMIN)
			return true;
		return false;
	}

	public static boolean canCreateAwsAccount(String type) {
		if (type.equals(ADMIN.toString()))
			return true;
		return false;
	}

	public static boolean canGiveSshAccess(UserType type) {
		if (type == ADMIN || type == MANAGER)
			return true;
		return false;
	}
	
	public static boolean canCreateTeam(UserType type) {
		if (type == ADMIN || type == MANAGER)
			return true;
		return false;
	}

	public static boolean canUpdateTeam(UserType type) {
		if (type == MANAGER)
			return true;
		return false;
	}
}