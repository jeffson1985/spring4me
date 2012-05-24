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

package org.osforce.spring4me.jdbc.core.metadata;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 2:04:56 PM
 */
public interface TableMetaDataContext {

	String getTableName();
	
	void setTableName(String tableName);
	
	String getCatalogName();
	
	void setCatalogName(String catalogName);
	
	String getSchemaName();
	
	void setSchemaName(String schemaName);
	
	String[] getGeneratedKeyNames();
	
	void setGeneratedKeyNames(String[] generatedKeyNames);
	
	List<String> getTableColumns();
	
	NativeJdbcExtractor getNativeJdbcExtractor();
	
	void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor);
	
	boolean isGeneratedKeysColumnNameArraySupported();
	
	void processMetaData(DataSource dataSource, List<String> declaredColumns);
	
	String createInsertString();
	
	int[] createInsertTypes();
	
	List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource parameterSource);
	
	String createUpdateString(String[] whereColumnNames);
	
	int[] createUpdateTypes(String[] whereColumnNames);
	
	List<Object> matchInParameterValuesWithUpdateColumns(SqlParameterSource parameterSource, String[] whereColumnNames);
	
	String createDeleteString(String[] whereColumnNames);
	
	int[] createDeleteTypes(String[] whereColumnNames);
	
	List<Object> matchInParameterValuesWithDeleteColumns(SqlParameterSource parameterSource, String[] whereColumnNames);
	
	String createSelectString(String[] whereColumnNames);
	
	int[] createSelectTypes(String[] whereColumnNames);
	
	List<Object> matchInParameterValuesWithSelectColumns(SqlParameterSource parameterSource, String[] whereColumnNames);
}
