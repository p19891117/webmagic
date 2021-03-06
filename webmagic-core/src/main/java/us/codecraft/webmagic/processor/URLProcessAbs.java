package us.codecraft.webmagic.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.exception.PageProcessException;

public abstract class URLProcessAbs implements URLProcess {
	protected static final Logger logger = LoggerFactory.getLogger(URLProcessAbs.class);
	private List<Pattern> patterns = new ArrayList<Pattern>();
	@Override
	public boolean matcher(String url) {
		if(StringUtils.isBlank(url))
			throw new IllegalArgumentException("请求的url不能为空");
		for(Pattern pattern:patterns){
			if(pattern.matcher(url).matches())
				return true;
		}
		return false;
	}
	@Override
	public void addPattern(String pattern) {
		if(StringUtils.isNotBlank(pattern)){
			this.patterns.add(Pattern.compile(pattern));
			logger.info("添加一个页面处理类["+this.getClass().getName()+"]pattern["+pattern+"]");
		}
	}
	@Override
	public void process(Task task, PageProcessor pageProcessor, Page page) throws PageProcessException {
		try{
			processPage(task,pageProcessor,page);
		}catch (Exception e) {
			throw new PageProcessException(e);
		}
	}
	protected abstract void processPage(Task task, PageProcessor pageProcessor, Page page)throws PageProcessException;
}
