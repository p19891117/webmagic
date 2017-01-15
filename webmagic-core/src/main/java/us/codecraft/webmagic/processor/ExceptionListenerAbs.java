package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.exception.NoPageProcessException;
import us.codecraft.webmagic.exception.PageDownloadException;
import us.codecraft.webmagic.exception.StatusCodeException;

public abstract class ExceptionListenerAbs implements ExceptionListener{

	@Override
	public void process(Page page, PageProcessor pageProcessor, Exception e) {
		if(e instanceof NoPageProcessException){
			processNPE(page, pageProcessor, (NoPageProcessException)e);
		}else if(e instanceof PageDownloadException){
			processPDE(page, pageProcessor, e.getCause());
		}else if(e instanceof StatusCodeException){
			processSCE(page, pageProcessor, (StatusCodeException)e);
		}
	}

	protected abstract  void processSCE(Page page, PageProcessor pageProcessor, StatusCodeException e);

	protected abstract  void processPDE(Page page, PageProcessor pageProcessor, Throwable e);

	protected abstract void processNPE(Page page, PageProcessor pageProcessor, NoPageProcessException e);

}
