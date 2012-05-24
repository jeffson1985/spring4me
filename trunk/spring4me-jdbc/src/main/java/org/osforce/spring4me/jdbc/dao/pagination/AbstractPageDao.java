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

package org.osforce.spring4me.jdbc.dao.pagination;

import org.osforce.spring4me.dao.pagination.Page;
import org.osforce.spring4me.dao.pagination.PageDao;
import org.osforce.spring4me.jdbc.dao.AbstractBaseDao;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-3-11 - ����4:05:57
 */
public class AbstractPageDao<T> extends AbstractBaseDao<T> 
	implements PageDao<T> {

	public AbstractPageDao(Class<T> mappedClass) {
		super(mappedClass);
	}

	public Page<T> fetchPage(Page<T> page) {
		return PageHelper.fetchPage(page, getJdbcTemplate(), getRowMapper(), getTableMetaDataContext());
	}
	
	public Page<T> fetchPage(Page<T> page, T model) {
		return PageHelper.fetchPage(page, getJdbcTemplate(), model, getRowMapper(), getTableMetaDataContext());
	}
	
}
