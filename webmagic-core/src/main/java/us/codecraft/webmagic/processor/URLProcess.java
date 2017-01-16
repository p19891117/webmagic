package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;

public interface URLProcess {
	/**
	 * 判断该url是否和本页面处理类匹配
	 * @param url
	 * @return
	 */
	boolean matcher(String url);
	/**
	 * 给页面处理类添加一个uri，用来匹配url
	 * @param uri
	 */
	public void addPattern(String pattern);
	/**
	 * 处理页面内容
	 * @param pageProcessor 
	 * @param page
	 */
	void process(PageProcessor pageProcessor, Page page);
}
