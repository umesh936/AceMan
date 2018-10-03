package in.sminfo.tool.mgmt.exception;

public class ObjectNotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ObjectNotFoundException() {
	}

	public ObjectNotFoundException(String reason) {
		super(reason);
	}

}
