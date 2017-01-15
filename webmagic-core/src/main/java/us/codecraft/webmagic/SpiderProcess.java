package us.codecraft.webmagic;

public interface SpiderProcess {
	void process();
	void putData(String key,Object data);
	<T> T getData(String key);
}
