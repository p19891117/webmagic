package us.codecraft.webmagic;

import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.URLProcessAbs;

public class SpiderTest {
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
	public void process(Task task, PageProcessor pageProcessor, Page page) {
		System.out.println(page.getRawText());
	}
	
}
