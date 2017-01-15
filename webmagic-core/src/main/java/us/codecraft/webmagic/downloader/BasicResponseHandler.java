package us.codecraft.webmagic.downloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.BufferedHttpEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.ContentType;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.StreamUtils;
import us.codecraft.webmagic.utils.UrlUtils;

public class BasicResponseHandler implements ResponseHandler<Page> {
	private static final Logger logger = LoggerFactory.getLogger(BasicResponseHandler.class);
	private Request request;
	private String charset;
	public BasicResponseHandler(Request request, String charset) {
		this.request = request;
		this.charset = charset;
	}
	@Override
	public Page handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
		String content = null;
		ContentType type = this.request.getType();
		int code = httpResponse.getStatusLine().getStatusCode();
		if(type==ContentType.TXT){
			content = toTxt(httpResponse);
		}else if(type==ContentType.BINARY){
			if(code>=200&&code<=299){
				File file = new File(new File(this.request.getFileDir()),this.request.getFileName());
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					httpResponse.getEntity().writeTo(out);
					content = file.getAbsolutePath();
				}finally{
					StreamUtils.close(out);
				}
			}else{
				content = toTxt(httpResponse);
			}
		}
        Page page = new Page();
        page.setRawText(content);
        page.setUrl(new PlainText(this.request.getUrl()));
        page.setRequest(this.request);
        page.setStatusCode(code);
        this.request.putExtra(Request.STATUS_CODE, code);
        return page;
	}
	private String toTxt(HttpResponse httpResponse) throws IOException {
		String content;
		BufferedHttpEntity entity = new BufferedHttpEntity(httpResponse.getEntity());
		String cs = getHtmlCharset(entity.getContentType(),entity.getContentEncoding(), entity.getContent());
		content = IOUtils.toString(entity.getContent(), cs);
		return content;
	}

    protected String getHtmlCharset(Header contentType, Header contentEncoding, InputStream inputStream) throws IOException {
        // 1、encoding in http header Content-Type
        if(contentType!=null){
        	String value = contentType.getValue();
        	String charset = UrlUtils.getCharset(value);
        	if (StringUtils.isNotBlank(charset)) {
        		logger.debug("Auto get charset(content-type): {}", charset);
        		return charset;
        	}
        }
        if(contentEncoding!=null){
        	String value = contentEncoding.getValue();
        	String charset = UrlUtils.getCharset(value);
        	if (StringUtils.isNotBlank(charset)) {
        		logger.debug("Auto get charset(content-encoding): {}", charset);
        		return charset;
        	}
        }
        // use default charset to decode first time
        String content = IOUtils.toString(inputStream, Charset.defaultCharset().name());
        // 2、charset in meta
        if (StringUtils.isNotEmpty(content)) {
            Document document = Jsoup.parse(content);
            Elements links = document.select("meta");
            String charset = null;
            for (Element link : links) {
                // 2.1、html4.01 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                String metaContent = link.attr("content");
                String metaCharset = link.attr("charset");
                if (metaContent.indexOf("charset") != -1) {
                    metaContent = metaContent.substring(metaContent.indexOf("charset"), metaContent.length());
                    logger.debug("Auto get charset(html-meta): {}", charset);
                    return metaContent.split("=")[1];
                }else if (StringUtils.isNotEmpty(metaCharset)) {// 2.2、html5 <meta charset="UTF-8" />
                	logger.debug("Auto get charset(html-meta): {}", charset);
                    return metaCharset;
                }
            }
        }
        if(this.charset!=null){
        	logger.debug("Auto get charset(site-configure): {}", charset);
        	return this.charset;
        }
        logger.debug("Auto get charset(default-utf-8): {}", charset);
        return "UTF-8";
    }
}
