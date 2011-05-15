package org.osforce.spring4me.task;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1
 * @create May 15, 2011 - 8:22:06 AM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface Task {

	void doSyncTask(Map<Object, Object> context);
	
	void doAsyncTask(Map<Object, Object> context);
	
}
