package us.codecraft.webmagic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.exception.PageProcessException;

public class SimURLProcess extends URLProcessAbs {
	private static final Logger logger = LoggerFactory.getLogger(SimURLProcess.class);
	public SimURLProcess() {
		addPattern("/");
	}
	@Override
	protected void processPage(Task task, PageProcessor pageProcessor, Page page) throws PageProcessException {
		logger.info(page.getRawText());
		
	}

}
