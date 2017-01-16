package us.codecraft.webmagic.scheduler.component;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;

public class SimRemover implements DuplicateRemover {
	private int count = 0;
	@Override
	public boolean isDuplicate(Request request, Task task) {
		count++;
		return false;
	}

	@Override
	public void resetDuplicateCheck(Task task) {

	}

	@Override
	public int getTotalRequestsCount(Task task) {
		return count;
	}

}
