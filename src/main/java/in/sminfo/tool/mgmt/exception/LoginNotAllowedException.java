package in.sminfo.tool.mgmt.exception;

public class LoginNotAllowedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginNotAllowedException() {
	}

	public LoginNotAllowedException(String reason) {
		super(reason);
	}

}
