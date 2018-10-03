package in.sminfo.tool.mgmt.exception;

public class UserAlreadyHaveAccessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserAlreadyHaveAccessException() {
	}

	public UserAlreadyHaveAccessException(String reason) {
		super(reason);
	}

}
