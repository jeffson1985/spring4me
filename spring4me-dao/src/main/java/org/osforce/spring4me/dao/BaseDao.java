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

package org.osforce.spring4me.dao;

import java.util.List;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 10:06:13 AM
 */
public interface BaseDao<T> {

	T load(Long id);
	
	void persist(T model);
	
	void merge(T model);
	
	void purge(T model);
	
	void purge(Long id);
	
	List<T> fetchList(T model);
	
	T fetchOne(T model);
	
}
