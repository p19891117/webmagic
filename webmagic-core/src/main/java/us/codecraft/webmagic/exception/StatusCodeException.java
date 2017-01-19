package us.codecraft.webmagic.exception;

public class StatusCodeException extends Exception {
	public static int arange100 = 1;
	public static int arange300 = 3;
	public static int arange400 = 4;
	public static int arange500 = 5;
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
	public StatusCodeException(Exception e) {
		super(e);
	}
	public int getStatuscode() {
		return statuscode;
	}
	public void setStatuscode(int statuscode) {
		this.statuscode = statuscode;
	}
	public int codeArange(){
		if(statuscode>=100&&statuscode<=199){
			return arange100;
		}else if(statuscode>=300&&statuscode<=399){
			return arange300;
		}else if(statuscode>=400&&statuscode<=499){
			return arange400;
		}else if(statuscode>=500&&statuscode<=599){
			return arange500;
		}
		return -1;
	}
}
