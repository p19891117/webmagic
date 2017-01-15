package us.codecraft.webmagic.utils;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamUtils {
	private static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);
	public static void close(Closeable stream){
		if(stream!=null){
			try {
				stream.close();
			} catch (IOException e) {
				logger.warn("关闭流失败",e);
			}
		}
	}
}
