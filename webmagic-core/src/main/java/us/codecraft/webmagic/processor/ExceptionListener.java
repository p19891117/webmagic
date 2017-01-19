package us.codecraft.webmagic.processor;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;

public interface ExceptionListener {

	public void process(Task task, Page page, PageProcessor pageProcessor, Exception e);
}
