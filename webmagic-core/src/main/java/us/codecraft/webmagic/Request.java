package us.codecraft.webmagic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import us.codecraft.webmagic.utils.Experimental;

/**
 * Object contains url to crawl.<br>
 * It contains some additional information.<br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class Request implements Serializable {

    private static final long serialVersionUID = 2062192774891352043L;

    public static final String CYCLE_TRIED_TIMES = "_cycle_tried_times";
    public static final String STATUS_CODE = "statusCode";
    public static final String PROXY = "proxy";
    
    private static final String namevaluepair = "nameValuePair";
    private static final String typekey = "contentTypeKey";
    
    private String url;

    private String method;

    /**
     * Store additional information in extras.
     */
    private Map<String, Object> extras;

    /**
     * Priority of the request.<br>
     * The bigger will be processed earlier. <br>
     * @see us.codecraft.webmagic.scheduler.PriorityScheduler
     */
    private long priority;

    public Request() {
    }

    public Request(String url) {
        this.url = url;
    }

    public long getPriority() {
        return priority;
    }

    /**
     * Set the priority of request for sorting.<br>
     * Need a scheduler supporting priority.<br>
     * @see us.codecraft.webmagic.scheduler.PriorityScheduler
     *
     * @param priority priority
     * @return this
     */
    @Experimental
    public Request setPriority(long priority) {
        this.priority = priority;
        return this;
    }

    public Object getExtra(String key) {
        if (extras == null) {
            return null;
        }
        return extras.get(key);
    }

    public Request putExtra(String key, Object value) {
        if (extras == null) {
            extras = new HashMap<String, Object>();
        }
        extras.put(key, value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (!url.equals(request.url)) return false;

        return true;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * The http method of the request. Get for default.
     * @return httpMethod
     * @see us.codecraft.webmagic.utils.HttpConstant.Method
     * @since 0.5.0
     */
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", extras=" + extras +
                ", priority=" + priority +
                '}';
    }
    /**
     * 获得post的请求参数集合
     * @return
     */
	public List<NameValuePair> getNameValuePairs() {
		@SuppressWarnings("unchecked")
		List<NameValuePair> nvps = (List<NameValuePair>) getExtra(namevaluepair);
		if(nvps==null)
			return new ArrayList<NameValuePair>();;
		Iterator<NameValuePair> iter = nvps.iterator();
		while(iter.hasNext()){
			if(iter.next()==null)
				iter.remove();
		}
		return nvps;
	}
	/**
	 * 为post设置参数，若name为kong，设置无效。
	 * @param name
	 * @param value
	 */
	public void putNameValuePair(String name,String value){
		@SuppressWarnings("unchecked")
		List<NameValuePair> nvps = (List<NameValuePair>) getExtra(namevaluepair);
		if(nvps==null){
			nvps = new ArrayList<NameValuePair>();
			putExtra(namevaluepair, nvps);
		}
		if(StringUtils.isBlank(name))
			return; 
		nvps.add(new BasicNameValuePair(name, value));
	}
	public void setType(ContentType type) {
		putExtra(typekey, type);
	}
	public ContentType getType() {
		ContentType  type = (ContentType) getExtra(typekey);
		if(type==null)
			type = ContentType.TXT;
		return type;
	}
	/**
	 * 当内容为文件下载时，设置该文件保存的目录，位于程序所在目录的相对目录
	 * @param dir
	 */
	public void setFileDir(String dir){
		putExtra("fileDirKey", dir);
	}
	/**
	 * 获取文件保存的目录
	 * @return
	 */
	public String getFileDir(){
		String dir = (String) getExtra("fileDirKey");
		if(StringUtils.isBlank(dir))
			dir = "workspace";
		return dir;
	}
	/**
	 * 当内容为文件下载时，设置该文件名称（包含文件后缀）
	 * @param fileName
	 */
	public void setFileName(String fileName){
		putExtra("fileNameKey", fileName);
	}
	/**
	 * 获取文件的名称
	 * @return
	 */
	public String getFileName(){
		String fileName = (String) getExtra("fileNameKey");
		if(StringUtils.isBlank(fileName))
			fileName = UUID.randomUUID().toString().replace("-", "").toUpperCase();;
		return fileName;
	}
	public static ReqBuilder custom(){
		return new ReqBuilder();
	}
}
