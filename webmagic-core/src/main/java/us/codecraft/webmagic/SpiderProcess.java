package us.codecraft.webmagic;

import java.util.Set;

public interface SpiderProcess {
	/**
	 * 执行数据存储
	 */
	void process();
	/**
	 * 根据key放置一个数据
	 * @param key
	 * @param data
	 */
	void putData(String key,Object data);
	/**
	 * 根据key获取一个数据
	 * @param key
	 * @return
	 */
	<T> T getData(String key);
	/**
	 * 获取所有key
	 * @return
	 */
	Set<String> getAllKey();
	/**
	 * 清空数据集合
	 */
	void clear();
}
