package in.sminfo.tool.mgmt.common.utilities;

public class Constant {

	public static interface MODEL {
		public final String USER_ID = "id";
		public final String USER_EMAIL = "email";
		public final String USER_NAME = "username";
		public final String AWS_NAME = "awsName";
	}
	public static interface SESSION {
		public final String USER = "user";
		public final String LOGGED_IN = "loggedin";
		public final String AWS_ACCOUNT_ID = "awsAccountId";
		public final String AWS_ACCOUNT_NAME = "awsAccountName";
	}
	public static interface DATA {
		public final String SELF_VPC = "self_vpc";
		public final String SELF_INSTANCE_ID = "self_instanceId";
	}
}
