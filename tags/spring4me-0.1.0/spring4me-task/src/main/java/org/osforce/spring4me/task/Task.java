package org.osforce.spring4me.task;

import java.util.Map;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:54:07 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface Task {

	void doSyncTask(Map<Object, Object> context);
	
	void doAsyncTask(Map<Object, Object> context);
	
}
