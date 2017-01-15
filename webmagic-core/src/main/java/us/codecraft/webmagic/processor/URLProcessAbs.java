package us.codecraft.webmagic.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public abstract class URLProcessAbs implements URLProcess {
	private  List<String> uris = new ArrayList<String>();
	@Override
	public boolean containURI(String url) {
		if(StringUtils.isBlank(url))
			throw new IllegalArgumentException("请求的url不能为空");
		for(String uri:uris)
			if(url.contains(uri))
				return true;
		return false;
	}
	@Override
	public void addURI(String uri) {
		if(StringUtils.isNotBlank(uri))
			this.uris.add(uri);
	}
}
