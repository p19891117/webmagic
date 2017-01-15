package us.codecraft.webmagic.exception;

public class PageDownloadException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4921543967730848136L;
	public PageDownloadException() {
	}
	public PageDownloadException(String msg) {
		super(msg);
	}
	public PageDownloadException(Exception e) {
		super(e);
	}
}
