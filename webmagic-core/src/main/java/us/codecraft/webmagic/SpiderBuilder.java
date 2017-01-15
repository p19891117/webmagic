package us.codecraft.webmagic;

import java.util.ArrayList;
import java.util.List;

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

public class SpiderBuilder{
	private PageProcessor pageProcessor;
	private SpiderProcess spiderProcess;
	private Downloader downloader;
	private List<Pipeline> pipelines = new ArrayList<Pipeline>();
	private Scheduler scheduler;
    private List<SpiderListener> spiderListeners = new ArrayList<SpiderListener>();
    
    private List<URLProcess> uRLProcess = new ArrayList<URLProcess>();
	private ExceptionListener exception;
	
	private List<Request> requests = new ArrayList<Request>();
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
    	if(downloader==null)
    		downloader = new HttpClientDownloader();
    	spider.setDownloader(downloader);
    	if(spiderProcess!=null)
    		spider.setSpiderProcess(spiderProcess);
    	if(pipelines.size()>0){
    		spider.setPipelines(pipelines);
    	}
    	if(scheduler==null){
    		scheduler = new QueueScheduler();
    	}
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
	public SpiderBuilder setSpiderProcess(SpiderProcess spiderProcess) {
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
}
