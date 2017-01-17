package us.codecraft.webmagic.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;

public class SimURLProcess extends URLProcessAbs {
	private static final Logger logger = LoggerFactory.getLogger(SimURLProcess.class);
	public SimURLProcess() {
		addPattern("/");
	}
	@Override
	public void process(Task task, PageProcessor pageProcessor, Page page) {
		logger.info(page.getRawText());
	}

}
