package us.codecraft.webmagic.interceptor;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpContext;

import us.codecraft.webmagic.Site;

public class UseGZipHttpRequestInterceptor implements org.apache.http.HttpRequestInterceptor {
	private Site site;
	public UseGZipHttpRequestInterceptor(Site site) {
		this.site = site;
	}

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		if (site == null || site.isUseGzip()) {
			if (!request.containsHeader("Accept-Encoding")) {
	            request.addHeader("Accept-Encoding", "gzip, deflate");
	        }
		}
	}

}
