/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.support.task;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-3-14 - ����1:39:02
 */
public interface Task {

	/**
	 * Synchronized task
	 * @param context
	 */
	void doSyncTask(TaskContext context) throws TaskException;
	
	/**
	 * Asynchronous task
	 * @param context
	 */
	void doAsyncTask(TaskContext context) throws TaskException;
	
}
