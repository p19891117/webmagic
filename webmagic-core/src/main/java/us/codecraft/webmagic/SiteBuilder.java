package us.codecraft.webmagic;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import us.codecraft.webmagic.utils.Configuer;

public class SiteBuilder {
	public Site build(){
		Site site = new Site();
		InputStream in = Configuer.readForStream("site.xml");
		if(in==null)
			in = Configuer.readForStream("site-default.xml");
		SAXReader sax = new SAXReader();
		Map<String, String> namespace = new HashMap<>();
		namespace.put("tns", "http://www.qysudu.com/");
		sax.getDocumentFactory().setXPathNamespaceURIs(namespace);
		try {
			Document doc = sax.read(in);
			org.dom4j.Node siteNode = doc.selectSingleNode("/tns:app/tns:site");
			String userAgent = siteNode.selectSingleNode("tns:userAgent").getText();
			String charset = siteNode.selectSingleNode("tns:charset").getText();
			int sleepTime = Integer.parseInt(siteNode.selectSingleNode("tns:sleepTime").getText());
			int retryTimes = Integer.parseInt(siteNode.selectSingleNode("tns:retryTimes").getText());
			int cycleRetryTimes = Integer.parseInt(siteNode.selectSingleNode("tns:cycleRetryTimes").getText());
			int retrySleepTime = Integer.parseInt(siteNode.selectSingleNode("tns:retrySleepTime").getText());
			int timeOut = Integer.parseInt(siteNode.selectSingleNode("tns:timeOut").getText());
			boolean useGzip = Boolean.parseBoolean(siteNode.selectSingleNode("tns:useGzip").getText());
			site.setUserAgent(userAgent);
			site.setCharset(charset);
			site.setSleepTime(sleepTime);
			site.setRetryTimes(retryTimes);
			site.setCycleRetryTimes(cycleRetryTimes);
			site.setRetrySleepTime(retrySleepTime);
			site.setTimeOut(timeOut);
			site.setUseGzip(useGzip);
			List<?> headObjs = siteNode.selectNodes("tns:Header/tns:key-value");
			if(headObjs!=null){
				for(Object headobj:headObjs){
					if(headobj==null)
						continue;
					Node node = (Node) headobj;
					String key = node.selectSingleNode("@name").getText();
					String value = node.selectSingleNode("@value").getText();
					if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
						site.addHeader(key, value);
					}
				}
			}
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
		return site;
	}
}
