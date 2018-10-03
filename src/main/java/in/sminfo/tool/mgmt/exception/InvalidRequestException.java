package in.sminfo.tool.mgmt.exception;

public class InvalidRequestException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRequestException() {
	}

	public InvalidRequestException(String reason) {
		super(reason);
	}

}
