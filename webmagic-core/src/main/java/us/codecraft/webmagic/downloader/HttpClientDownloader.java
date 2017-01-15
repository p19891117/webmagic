package us.codecraft.webmagic.downloader;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.exception.PageDownloadException;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.HttpConstant;


/**
 * The http downloader based on HttpClient.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
@ThreadSafe
public class HttpClientDownloader extends AbstractDownloader {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();


    private CloseableHttpClient getHttpClient(Site site, Proxy proxy) {
        if (site == null) {
            return getClient(null, proxy);
        }
        String domain = site.getDomain();
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = getClient(site, proxy);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }
    private HttpContext getHttpContext(Site site) {
        String domain = site.getDomain();
        HttpContext httpContext = getHttpContexts().get(domain);
        if (httpContext == null) {
            synchronized (this) {
            	httpContext = getHttpContexts().get(domain);
                if (httpContext == null) {
                	httpContext = new BasicHttpContext();
                	getHttpContexts().put(domain, httpContext);
                }
            }
        }
        return httpContext;
    }
    @Override
    public Page download(Request request, Task task) {
        Site site = task.getSite();
        logger.info("downloading page {}", request.getUrl());
        HttpHost proxyHost = null;
        Proxy proxy = null;
        boolean proxyPoolEnable = site.getHttpProxyPool() != null && site.getHttpProxyPool().isEnable();
        if (proxyPoolEnable) {
        	proxy = site.getHttpProxyFromPool();
        	proxyHost = proxy.getHttpHost();
        } else if(site.getHttpProxy()!= null){
        	proxyHost = site.getHttpProxy();
        }            
        try {
            HttpUriRequest httpUriRequest = getHttpUriRequest(request, site , proxyHost);
            CloseableHttpClient client = getHttpClient(site, proxy);
            HttpContext httpContext = getHttpContext(site);
            Page page = client.execute(httpUriRequest,new BasicResponseHandler(request,site.getCharset()), httpContext);
            onSuccess(request);
            return page;
        } catch (Exception e) {
        	request.putExtra(Request.STATUS_CODE, 0);
            if (site.getCycleRetryTimes() > 0) {
                return addToCycleRetry(request, site);
            }else{
            	Page page = new Page();
                page.setRawText(e.getMessage());
                page.setUrl(new PlainText(request.getUrl()));
                request.putExtra("httpExcetpionKey", new PageDownloadException(e));
                page.setRequest(request);
                page.setStatusCode(-1);
                onError(request);
                return page;
            }
        } finally {
            if (proxyPoolEnable)
                site.returnHttpProxyToPool((HttpHost) request.getExtra(Request.PROXY), (Integer) request.getExtra(Request.STATUS_CODE));
        }
    }

    protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
        return acceptStatCode.contains(statusCode);
    }

    protected HttpUriRequest getHttpUriRequest(Request request, Site site, HttpHost proxy) {
    	String method = request.getMethod();
    	RequestBuilder requestBuilder = null;
        if (method == null || method.equalsIgnoreCase(HttpConstant.Method.GET)) {
        	requestBuilder = RequestBuilder.get();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.POST)) {
            requestBuilder = RequestBuilder.post();
            List<NameValuePair> nvps = request.getNameValuePairs();
            if (nvps.size() > 0) {
                try {
					requestBuilder.setEntity(new UrlEncodedFormEntity(nvps,site.getCharset()));
				} catch (UnsupportedEncodingException e) {
					logger.error("设置post请求参数编码出错",e);
				}
            }
        } else if (method.equalsIgnoreCase(HttpConstant.Method.HEAD)) {
        	requestBuilder = RequestBuilder.head();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.PUT)) {
        	requestBuilder = RequestBuilder.put();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.DELETE)) {
        	requestBuilder = RequestBuilder.delete();
        } else if (method.equalsIgnoreCase(HttpConstant.Method.TRACE)) {
        	requestBuilder = RequestBuilder.trace();
        }else{
        	throw new IllegalArgumentException("Illegal HTTP Method " + method);
        }
        requestBuilder.setUri(request.getUrl());
        Map<String, String> headers = site.getHeaders();
        for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
            requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setConnectionRequestTimeout(site.getTimeOut())
                .setSocketTimeout(site.getTimeOut())
                .setConnectTimeout(site.getTimeOut())
                .setCookieSpec(CookieSpecs.STANDARD_STRICT);
        if (proxy !=null) {
			requestConfigBuilder.setProxy(proxy);
			request.putExtra(Request.PROXY, proxy);
		}
        requestBuilder.setConfig(requestConfigBuilder.build());
        return requestBuilder.build();
    }
    @Override
    public void setThread(int thread) {
    	connectionManager().setMaxTotal(thread);
    }
}
