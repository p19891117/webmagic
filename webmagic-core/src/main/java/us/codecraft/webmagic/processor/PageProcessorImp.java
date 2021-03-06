package us.codecraft.webmagic.processor;

import java.util.ArrayList;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.exception.NoPageProcessException;
import us.codecraft.webmagic.exception.StatusCodeException;

public class PageProcessorImp implements PageProcessor{
	private Site site;
	private List<URLProcess> uRLProcess = new ArrayList<URLProcess>();
	private ExceptionListener exception = new SimExceptionListenerImp();
	public PageProcessorImp(){}
	public PageProcessorImp(Site site){this.site = site;}
	@Override
	public Site getSite() {
		return site;
	}
	public void setSite(Site site){
		this.site = site;
	}
	@Override
	public void process(Task task,Page page) {
		try {
			int statusCode = page.getStatusCode();
			if(statusCode>=100&&statusCode<=199){
				StatusCodeException e = new StatusCodeException("http响应码["+statusCode+"]非200区间错误");
				e.setStatuscode(statusCode);
				throw e;
			}else if(statusCode>=200&&statusCode<=299){
				boolean flag = false;
				String url = page.getRequest().getUrl();
				for(URLProcess uRLProces:uRLProcess){
					if(!uRLProces.matcher(url))
						continue;
					uRLProces.process(task,this,page);
					flag = true;
					break;
				}
				if(!flag)
					throw new NoPageProcessException("缺少处理类处理该url["+url+"]");
			}else if(statusCode>=300&&statusCode<=399){
				StatusCodeException e = new StatusCodeException("http响应码["+statusCode+"]非200区间错误");
				e.setStatuscode(statusCode);
				throw e;
			}else if(statusCode>=400&&statusCode<=499){
				StatusCodeException e = new StatusCodeException("http响应码["+statusCode+"]非200区间错误");
				e.setStatuscode(statusCode);
				throw e;
			}else if(statusCode>=500&&statusCode<=599){
				StatusCodeException e = new StatusCodeException("http响应码["+statusCode+"]非200区间错误");
				e.setStatuscode(statusCode);
				throw e;
			}else if(statusCode==-1){
				throw (Exception) page.getRequest().getExtra("httpExcetpionKey");
			}else{
				StatusCodeException e = new StatusCodeException("http响应码["+statusCode+"]非200区间错误");
				e.setStatuscode(statusCode);
				throw e;
			}
		} catch (Exception e) {
			page.getRequest().addOccurError();
			exception.process(task,page,this,e);
		}
	}
	public void setuRLProcess(List<URLProcess> uRLProcess) {
		this.uRLProcess = uRLProcess;
	}
	public void setException(ExceptionListener exception) {
		this.exception = exception;
	}
	public static PageProcessBuilder custom(){
		return new PageProcessBuilder();
	}
}
