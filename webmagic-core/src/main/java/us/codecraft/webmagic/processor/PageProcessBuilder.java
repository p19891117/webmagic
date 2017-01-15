package us.codecraft.webmagic.processor;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Site;

public class PageProcessBuilder {
	private Site site;
	private List<URLProcess> uRLProcess = new ArrayList<URLProcess>();
	private ExceptionListener exception;
	public PageProcessor build(){
		if(site==null)
			site = Site.custom().build();
		PageProcessorImp imp = new PageProcessorImp(site);
		if(uRLProcess.size()<=0)
			uRLProcess.add(new SimURLProcess());
		imp.setuRLProcess(uRLProcess);
		if(exception!=null)
			imp.setException(exception);
		return imp;
	}
	public PageProcessBuilder setSite(Site site) {
		this.site = site;
		return this;
	}
	public PageProcessBuilder setException(ExceptionListener exception) {
		this.exception = exception;
		return this;
	}
	public PageProcessBuilder setuRLProcess(List<URLProcess> uRLPs) {
		if(uRLPs==null||uRLPs.size()<=0)
			return this;
		this.uRLProcess.addAll(uRLPs);
		return this;
	}
	public PageProcessBuilder adduRLProcess(URLProcess uRLP) {
		if(uRLP==null)
			return this;
		this.uRLProcess.add(uRLP);
		return this;
	}
}
