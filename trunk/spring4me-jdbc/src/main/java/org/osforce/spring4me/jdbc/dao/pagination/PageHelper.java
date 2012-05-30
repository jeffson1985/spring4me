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

import java.sql.ResultSet;
import java.util.List;

import org.osforce.spring4me.dao.pagination.Page;
import org.osforce.spring4me.jdbc.core.extractor.LimitedRowMapperResultSetExtractor;
import org.osforce.spring4me.jdbc.core.metadata.TableMetaDataContext;
import org.osforce.spring4me.jdbc.utils.ModelUtils;
import org.osforce.spring4me.jdbc.utils.SqlUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 3:41:31 PM
 */
public abstract class PageHelper {
	
	public static <T> Page<T> fetchPage(Page<T> page, JdbcTemplate jdbcTemplate, 
			RowMapper<T> rowMapper, TableMetaDataContext tableMetaDataContext) {
		String selectString = tableMetaDataContext.createSelectString(null);
		LimitedRowMapperResultSetExtractor<T> rse = new LimitedRowMapperResultSetExtractor<T>(
				page.getFirst(), page.getPageSize(), rowMapper);
		//
		if(page.getAutoCount() && page.getTotalCount()!=Page.TOTAL_COUNT_UNKNOW) {
			String countString = SqlUtils.getCountSql(selectString);
			Long totalCount = jdbcTemplate.queryForLong(countString);
			page.setTotalCount(totalCount);
		}
		//
		PreparedStatementCreator psc = newPreparedStatementCreator(selectString);
		List<T> results = jdbcTemplate.query(psc, rse);
		return page.setResult(results);
	}

	public static <T> Page<T> fetchPage(Page<T> page, JdbcTemplate jdbcTemplate, T model, 
			RowMapper<T> rowMapper, TableMetaDataContext tableMetaDataContext) {
		String[] filterFields = ModelUtils.getFilterFields(model);
		LimitedRowMapperResultSetExtractor<T> rse = new LimitedRowMapperResultSetExtractor<T>(
				page.getFirst(), page.getPageSize(), rowMapper);
		//
		String[] whereColumnNames = SqlUtils.getWhereColumnNames(tableMetaDataContext.getGeneratedKeyNames(), filterFields);
		//
		String selectString = tableMetaDataContext.createSelectString(whereColumnNames);
		int[] selectTypes = tableMetaDataContext.createDeleteTypes(whereColumnNames);
		SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(model);
		List<Object> values = tableMetaDataContext.matchInParameterValuesWithDeleteColumns(sqlParameterSource, whereColumnNames);
		//
		if(page.getAutoCount() && page.getTotalCount()!=Page.TOTAL_COUNT_UNKNOW) {
			String countString = SqlUtils.getCountSql(selectString);
			Long totalCount = jdbcTemplate.queryForLong(countString, values.toArray(), selectTypes);
			page.setTotalCount(totalCount);
		}
		//
		PreparedStatementCreator psc = newPreparedStatementCreator(selectString, selectTypes, values);
		List<T> results = jdbcTemplate.query(psc, rse);
		return page.setResult(results);
	}
	
	private static PreparedStatementCreator newPreparedStatementCreator(String sql) {
		return newPreparedStatementCreator(sql, null, null);
	}
	
	private static PreparedStatementCreator newPreparedStatementCreator(String sql, int[] types, List<Object> values) {
		PreparedStatementCreatorFactory pscf = null;
		if(types==null) {
			pscf = new PreparedStatementCreatorFactory(sql);
		} else {
			pscf = new PreparedStatementCreatorFactory(sql, types);
		}
		//
		pscf.setResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE);
		//
		return pscf.newPreparedStatementCreator(values);
	}
	
}
