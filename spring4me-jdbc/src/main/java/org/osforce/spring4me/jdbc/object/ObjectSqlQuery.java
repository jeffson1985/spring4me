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

package org.osforce.spring4me.jdbc.object;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.object.SqlQuery;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 11, 2012 - 6:30:11 PM
 */
public class ObjectSqlQuery<T> extends SqlQuery<T> {

	private Class<T> mappedClass;
	
	public ObjectSqlQuery() {
	}
	
	public ObjectSqlQuery(DataSource dataSource, String sql) {
		super(dataSource, sql);
	}
	
	public void setMappedClass(Class<T> mappedClass) {
		this.mappedClass = mappedClass;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected RowMapper<T> newRowMapper(Object[] parameters, Map context) {
		return new BeanPropertyRowMapper<T>(mappedClass);
	}

}
