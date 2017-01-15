package us.codecraft.webmagic.exception;

public class StatusCodeException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2166077679474406565L;
	private int statuscode;
	public StatusCodeException() {
	}
	public StatusCodeException(String msg) {
		super(msg);
	}
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
}
