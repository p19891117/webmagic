package us.codecraft.webmagic;

import java.util.Map;

public interface SpiderExtraProcess {
	/**
	 * 执行数据存储
	 */
	void process(Map<String,Object> extra);
}
