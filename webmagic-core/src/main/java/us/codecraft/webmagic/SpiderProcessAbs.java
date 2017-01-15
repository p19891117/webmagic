package us.codecraft.webmagic;

import java.util.HashMap;
import java.util.Map;

public abstract class SpiderProcessAbs implements SpiderProcess {
	private Map<String, Object> data = new HashMap<String, Object>();
	@Override
	public void putData(String key, Object data) {
		this.data.put(key, data);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getData(String key) {
		return (T) this.data.get(key);
	}

}
