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

package org.osforce.spring4me.jdbc.core.extractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 11, 2012 - 9:53:57 PM
 */
public class LimitedRowMapperResultSetExtractor<T> 
	implements ResultSetExtractor<List<T>> {

	private int firstResult = 0;
	private int maxResults = 0;
	private final RowMapper<T> rowMapper;

	public LimitedRowMapperResultSetExtractor(int firstResult, int maxResults, RowMapper<T> rowMapper) {
		this.firstResult = firstResult;
		this.maxResults = maxResults;
		this.rowMapper = rowMapper;
	}
	
	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}
	
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

	public List<T> extractData(ResultSet rs) throws SQLException {
		// skip rows
		skipRows(rs);
		//
		List<T> results = new ArrayList<T>(this.maxResults);
		int resultNum = 0;
		while (resultNum<this.maxResults && rs.next()) {
			results.add(this.rowMapper.mapRow(rs, resultNum++));
		}
		return results;
	}
	
	private void skipRows(ResultSet rs) throws SQLException {
		if(ResultSet.TYPE_FORWARD_ONLY==rs.getType()) {
			int rowNum=1;
			while(rowNum<this.firstResult && rs.next()) {
				rowNum++;
			}
		} else {
			rs.absolute(this.firstResult-1);
		}
	}
	
}
