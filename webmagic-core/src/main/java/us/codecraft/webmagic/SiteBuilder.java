package us.codecraft.webmagic;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.utils.Configuer;
import us.codecraft.webmagic.utils.Dom4jUtils;

public class SiteBuilder {
	private static final Logger logger = LoggerFactory.getLogger(SiteBuilder.class);
	public Site build(){
		Site site = new Site();
		InputStream in = Configuer.readForStream("site.xml");
		if(in==null)
			in = Configuer.readForStream("site-default.xml");
		Map<String, String> namespace = new HashMap<>();
		namespace.put("s", "http://www.qysudu.com/site");
		namespace.put("k", "http://www.qysudu.com/kafka");
		namespace.put("m", "http://www.qysudu.com/mysql");
		try {
			Dom4jUtils utils = Dom4jUtils.custom(in).setNamespace(namespace).build();
			site.setUserAgent(utils.nodeText(("/s:app/s:site/s:userAgent")));
			site.setCharset(utils.nodeText(("/s:app/s:site/s:charset")));
			site.setSleepTime(Integer.parseInt(utils.nodeText(("/s:app/s:site/s:sleepTime"))));
			site.setRetryTimes(Integer.parseInt(utils.nodeText(("/s:app/s:site/s:retryTimes"))));
			site.setCycleRetryTimes(Integer.parseInt(utils.nodeText(("/s:app/s:site/s:cycleRetryTimes"))));
			site.setRetrySleepTime(Integer.parseInt(utils.nodeText(("/s:app/s:site/s:retrySleepTime"))));
			site.setTimeOut(Integer.parseInt(utils.nodeText(("/s:app/s:site/s:timeOut"))));
			site.setUseGzip(Boolean.parseBoolean(utils.nodeText(("/s:app/s:site/s:useGzip"))));
			List<Map<String, String>> headMaps = utils.nodeAtts(("/s:app/s:site/s:Header/s:key-value"));
			if(headMaps!=null){
				for(Map<String, String> headMap:headMaps){
					String key = headMap.get("name");
					String value = headMap.get("value");
					if(StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
						site.addHeader(key, value);
					}
				}
			}
			return site;
		} catch (DocumentException e) {
			logger.error("读取site的配置文件出错",e);
			System.exit(0);
			return null;
		}
	}
}
