package in.sminfo.tool.mgmt.exception;

public class GenericRuntimeException extends RuntimeException{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String exceptionMsg;

	public GenericRuntimeException(String exceptionMsg) {
	        super(exceptionMsg);
		this.exceptionMsg = exceptionMsg;
	}
	
	public String getExceptionMsg(){
		return this.exceptionMsg;
	}
	
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
}
