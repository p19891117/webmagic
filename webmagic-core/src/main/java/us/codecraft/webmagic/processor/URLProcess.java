package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.exception.PageProcessException;

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
	 * @param task 
	 * @param pageProcessor 
	 * @param page
	 */
	void process(Task task, PageProcessor pageProcessor, Page page) throws PageProcessException ;
}
