package us.codecraft.webmagic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.exception.PageProcessException;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.URLProcessAbs;

public class SpiderTest {
	private Logger logger = LoggerFactory.getLogger(SpiderTest.class); 
	public static void main(String[] args) {
		Request req = Request.custom().setUrl("https://www.baidu.com/").build();
		Spider spider = Spider.custom().addRequests(req)
				.adduRLProcess(new Baidu())
				.build();
		spider.start();
	}
}
class Baidu extends URLProcessAbs{
	public Baidu(){
		addPattern("^https://www.baidu.com/$");
	}
	@Override
	protected void processPage(Task task, PageProcessor pageProcessor, Page page) throws PageProcessException {
		logger.info(page.getRawText());
	}
}
