package us.codecraft.webmagic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.ExceptionListener;
import us.codecraft.webmagic.processor.PageProcessBuilder;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.PageProcessorImp;
import us.codecraft.webmagic.processor.URLProcess;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;
import us.codecraft.webmagic.scheduler.component.SimRemover;

public class SpiderBuilder{
	/**
	 * 设置pageprocess
	 */
	private PageProcessor pageProcessor;
	/**
	 * 设置spider级别的数据存储处理
	 */
	private SpiderExtraProcess spiderProcess;
	/**
	 * 设置downloader
	 */
	private Downloader downloader;
	/**
	 * 设置page级别的数据存储处理
	 */
	private List<Pipeline> pipelines = new ArrayList<Pipeline>();
	/**
	 * 设置url管理
	 */
	private Scheduler scheduler;
	/**
	 * 设置url驱虫策略
	 */
	private DuplicateRemover duplicateRemover;
	/**
	 * 设置spiderlistener
	 */
    private List<SpiderListener> spiderListeners = new ArrayList<SpiderListener>();
    /**
     * 设置url处理类
     */
    private List<URLProcess> uRLProcess = new ArrayList<URLProcess>();
    /**
     * 设置异常处理策略
     */
	private ExceptionListener exception;
	/**
	 * 设置开始url
	 */
	private List<Request> requests = new ArrayList<Request>();
	private Map<String, Object> extra = new HashMap<String, Object>();
    public Spider build(){
    	if(pageProcessor==null){
    		PageProcessBuilder build = PageProcessorImp.custom();
    		if(exception!=null)
    			build.setException(exception);
    		if(uRLProcess.size()>0)
    			build.setuRLProcess(uRLProcess);
    		pageProcessor = build.build();
    	}
    	Spider spider = new Spider(pageProcessor);
    	for(Entry<String, Object> entry:extra.entrySet())
    		spider.putExtra(entry.getKey(), entry.getValue());
    	if(downloader==null)
    		downloader = new HttpClientDownloader();
    	spider.setDownloader(downloader);
    	if(spiderProcess!=null)
    		spider.setSpiderProcess(spiderProcess);
    	if(pipelines.size()>0){
    		spider.setPipelines(pipelines);
    	}
    	if(scheduler==null)
    		scheduler = new QueueScheduler();
    	if(duplicateRemover==null)
    		duplicateRemover = new SimRemover();
    	((QueueScheduler)scheduler).setDuplicateRemover(duplicateRemover);
    	spider.setScheduler(scheduler);
    	if(spiderListeners.size()>0){
    		spider.setSpiderListeners(spiderListeners);
    	}
    	if(requests.size()<=0)
    		throw new IllegalArgumentException("初始化url不能为空");
    	for(Request req:requests)
    		spider.addRequest(req);
    	if(StringUtils.isNotBlank(spider.getSite().getDomain()))
    		spider.getSite().addHeader("Host", spider.getSite().getDomain());
    	return spider;
    }
	public SpiderBuilder setPageProcessor(PageProcessor pageProcessor) {
		this.pageProcessor = pageProcessor;
		return this;
	}
	public SpiderBuilder setDownloader(Downloader downloader) {
		this.downloader = downloader;
		return this;
	}
	public SpiderBuilder addPipelines(Pipeline pipeline) {
		this.pipelines.add(pipeline);
		return this;
	}
	public SpiderBuilder setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		return this;
	}
	public SpiderBuilder addSpiderListeners(SpiderListener spiderListener) {
		this.spiderListeners.add(spiderListener);
		return this;
	}
	public SpiderBuilder addRequests(Request ...requests) {
		if(requests==null||requests.length<=0)
			return this;
		for(Request req:requests){
			if(req==null)
				continue;
			this.requests.add(req);
		}
		return this;
	}
	public SpiderBuilder addRequests(String ...urls) {
		if(urls==null||urls.length<=0)
			return this;
		for(String url:urls){
			if(StringUtils.isBlank(url))
				continue;
			this.requests.add(new Request(url));
		}
		return this;
	}
	public SpiderBuilder setSpiderProcess(SpiderExtraProcess spiderProcess) {
		this.spiderProcess = spiderProcess;
		return this;
	}
	public SpiderBuilder setuRLProcess(List<URLProcess> uRLProcess) {
		if(uRLProcess==null||uRLProcess.size()<=0)
			return this;
		this.uRLProcess.addAll(uRLProcess);
		return this;
	}
	public SpiderBuilder adduRLProcess(URLProcess uRLProcess) {
		if(uRLProcess==null)
			return this;
		this.uRLProcess.add(uRLProcess);
		return this;
	}
	public SpiderBuilder setDuplicateRemover(DuplicateRemover duplicateRemover) {
		this.duplicateRemover = duplicateRemover;
		return this;
	}
	public SpiderBuilder setExceptionListener(ExceptionListener exception) {
		this.exception = exception;
		return this;
	}
	public SpiderBuilder putExtra(String key,Object value){
		if(StringUtils.isBlank(key))
			return this;
		extra.put(key, value);
		return this;
	}
}
