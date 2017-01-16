package us.codecraft.webmagic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class SpiderProcessAbs implements SpiderProcess {
	protected Map<String, Object> data = new HashMap<String, Object>();
	@Override
	public void putData(String key, Object data) {
		this.data.put(key, data);
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getData(String key) {
		return (T) this.data.get(key);
	}
	@Override
	public Set<String> getAllKey() {
		return this.data.keySet();
	}
	@Override
	public void clear() {
		this.data.clear();
	}

}
