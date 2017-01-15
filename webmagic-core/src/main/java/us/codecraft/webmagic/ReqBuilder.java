package us.codecraft.webmagic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.utils.HttpConstant.Method;

public class ReqBuilder {
	private String url;
	private String method = Method.GET;
	private Map<String,String> params;
	private Map<String, Object> extras = new HashMap<String, Object>();
	public Request build(){
		Request request = new Request(url);
		request.setMethod(method);
		if(extras.size()>0)
			request.setExtras(extras);
		if(Method.POST.equals(method)&&params!=null){
			for(Entry<String, String> entry:params.entrySet())
			request.putNameValuePair(entry.getKey(), entry.getValue());
		}
		return request;
	}
	public Request buildPost(){
		method = Method.POST;
		return build();
	}
	public ReqBuilder setUrl(String url) {
		if(StringUtils.isBlank(url))
			throw new IllegalArgumentException("参数错误,url为空");
		this.url = url;
		return this;
	}
	public ReqBuilder setMethod(String method) {
		if(StringUtils.isBlank(method))
			throw new IllegalArgumentException("参数错误,method为空");
		this.method = method;
		return this;
	}
	public ReqBuilder addParams(String key,String value) {
		if(params==null)
			params = new HashMap<>();
		if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value))
			this.params.put(key, value);
		return this;
	}
	public ReqBuilder addExtras(String key,Object value) {
		this.extras.put(key, value);
		return this;
	}
}
