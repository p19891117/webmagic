package us.codecraft.webmagic.exception;

public class DocumentException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4921543967730848136L;
	public DocumentException() {
	}
	public DocumentException(String msg) {
		super(msg);
	}
	public DocumentException(Exception e) {
		super(e);
	}
}
