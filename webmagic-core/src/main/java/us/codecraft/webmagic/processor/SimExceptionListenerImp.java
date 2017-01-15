package us.codecraft.webmagic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.exception.NoPageProcessException;
import us.codecraft.webmagic.exception.StatusCodeException;

public class SimExceptionListenerImp extends ExceptionListenerAbs {
	private static final Logger logger = LoggerFactory.getLogger(ExceptionListenerAbs.class);
	@Override
	protected void processSCE(Page page, PageProcessor pageProcessor, StatusCodeException e) {
		logger.error("出错",e);
	}

	@Override
	protected void processPDE(Page page, PageProcessor pageProcessor, Throwable e) {
		logger.error("出错",e);
	}

	@Override
	protected void processNPE(Page page, PageProcessor pageProcessor, NoPageProcessException e) {
		logger.error("出错",e);
	}
}
