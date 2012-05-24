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

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.osforce.spring4me.jdbc.core.extractor.LimitedRowMapperResultSetExtractor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 11, 2012 - 6:30:24 PM
 */
public class LimitedObjectSqlQuery<T> extends ObjectSqlQuery<T> {
	
	private int firstResult;
	private int maxResults;
	
	public LimitedObjectSqlQuery() {
		setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
	}
	
	public LimitedObjectSqlQuery(DataSource dataSource, String sql) {
		super(dataSource, sql);
		setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
	}

	public int getFirstResult() {
		return firstResult;
	}
	
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
	
	public int getMaxResults() {
		return maxResults;
	}
	
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List<T> execute(Object[] params, Map context) throws DataAccessException {
		validateParameters(params);
		RowMapper<T> rowMapper = newRowMapper(params, context);
		ResultSetExtractor<List<T>> rse = new LimitedRowMapperResultSetExtractor<T>(
				getFirstResult(), getMaxResults(), rowMapper);
		return getJdbcTemplate().query(newPreparedStatementCreator(params), rse);
	}
	
}
