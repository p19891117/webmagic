package us.codecraft.webmagic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;

public class SimURLProcess extends URLProcessAbs {
	private static final Logger logger = LoggerFactory.getLogger(SimURLProcess.class);
	public SimURLProcess() {
		addPattern("/");
	}
	@Override
	public void process(PageProcessor pageProcessor, Page page) {
		logger.info(page.getRawText());
	}

}
