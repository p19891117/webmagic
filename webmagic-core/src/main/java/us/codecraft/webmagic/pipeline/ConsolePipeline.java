package us.codecraft.webmagic.pipeline;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

/**
 * Write results in console.<br>
 * Usually used in test.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class ConsolePipeline implements Pipeline {
	private Logger logger = LoggerFactory.getLogger(ConsolePipeline.class); 
    @Override
    public void process(ResultItems resultItems, Task task) {
    	//logger.info("get page: " + resultItems.getRequest().getUrl());
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
        	logger.info(entry.getKey() + ":\t" + entry.getValue());
        }
    }
}
