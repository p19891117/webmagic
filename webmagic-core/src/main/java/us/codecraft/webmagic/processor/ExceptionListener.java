package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;

public interface ExceptionListener {

	public void process(Page page, PageProcessor pageProcessor, Exception e);
}
