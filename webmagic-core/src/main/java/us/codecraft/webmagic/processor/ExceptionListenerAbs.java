package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.exception.NoPageProcessException;
import us.codecraft.webmagic.exception.PageDownloadException;
import us.codecraft.webmagic.exception.PageProcessException;
import us.codecraft.webmagic.exception.StatusCodeException;

public abstract class ExceptionListenerAbs implements ExceptionListener{

	@Override
	public void process(Task task, Page page, PageProcessor pageProcessor, Exception e) {
		if(e instanceof NoPageProcessException){
			processNPE(task, page, pageProcessor, (NoPageProcessException)e);
		}else if(e instanceof PageDownloadException){
			processPDE(task, page, pageProcessor, e.getCause());
		}else if(e instanceof StatusCodeException){
			processSCE(task, page, pageProcessor, (StatusCodeException)e);
		}else if(e instanceof PageProcessException ){
			processPPE(task, page, pageProcessor, e.getCause());
		}
	}
	protected abstract void processPPE(Task task, Page page, PageProcessor pageProcessor, Throwable cause) ;
	/**
	 * 下载过程出现状态码区间非200时，抛异常
	 * @param page
	 * @param pageProcessor
	 * @param e
	 */
	protected abstract  void processSCE(Task task, Page page, PageProcessor pageProcessor, StatusCodeException e);
	/**
	 * 调用httpclient组件进行下载过程中出现异常。
	 * @param page
	 * @param pageProcessor
	 * @param e
	 */
	protected abstract  void processPDE(Task task, Page page, PageProcessor pageProcessor, Throwable e);
	/**
	 * 当页面没有对应处理类处理，抛异常
	 * @param page
	 * @param pageProcessor
	 * @param e
	 */
	protected abstract void processNPE(Task task, Page page, PageProcessor pageProcessor, NoPageProcessException e);

}
