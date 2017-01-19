package us.codecraft.webmagic.exception;

public class PageProcessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 166915778208674787L;
	public PageProcessException() {
	}
	public PageProcessException(String msg) {
		super(msg);
	}
	public PageProcessException(Exception e) {
		super(e);
	}
}
