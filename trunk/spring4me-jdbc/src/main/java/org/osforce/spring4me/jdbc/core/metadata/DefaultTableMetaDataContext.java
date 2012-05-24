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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.metadata.TableMetaDataProvider;
import org.springframework.jdbc.core.metadata.TableMetaDataProviderFactory;
import org.springframework.jdbc.core.metadata.TableParameterMetaData;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;


/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 11:07:12 AM
 */
public class DefaultTableMetaDataContext implements TableMetaDataContext {
	
	private String tableName;
	private String schemaName;
	private String catalogName;
	//
	private String[] generatedKeyNames;
	//
	private TableMetaDataProvider metaDataProvider;
	//
	private NativeJdbcExtractor nativeJdbcExtractor;
	
	private org.springframework.jdbc.core.metadata.TableMetaDataContext tableMetaDataContext;
	
	public DefaultTableMetaDataContext() {
		this.tableMetaDataContext = new org.springframework.jdbc.core.metadata.TableMetaDataContext();
	}
	
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getCatalogName() {
		return this.catalogName;
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getSchemaName() {
		return this.schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public String[] getGeneratedKeyNames() {
		return this.generatedKeyNames;
	}

	public void setGeneratedKeyNames(String[] generatedKeyNames) {
		this.generatedKeyNames = generatedKeyNames;
	}
	
	public NativeJdbcExtractor getNativeJdbcExtractor() {
		return this.nativeJdbcExtractor;
	}

	public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor) {
		this.nativeJdbcExtractor = nativeJdbcExtractor;
	}
	
	public boolean isGeneratedKeysColumnNameArraySupported() {
		return tableMetaDataContext.isGeneratedKeysColumnNameArraySupported();
	}
	
	public void processMetaData(DataSource dataSource,
			List<String> declaredColumns) {
		this.tableMetaDataContext.setTableName(getTableName());
		this.tableMetaDataContext.setSchemaName(getSchemaName());
		this.tableMetaDataContext.setCatalogName(getCatalogName());
		this.tableMetaDataContext.setNativeJdbcExtractor(getNativeJdbcExtractor());
		this.tableMetaDataContext.processMetaData(dataSource, declaredColumns, getGeneratedKeyNames());
		//
		this.metaDataProvider = TableMetaDataProviderFactory
				.createMetaDataProvider(dataSource, tableMetaDataContext, getNativeJdbcExtractor());
	}
	
	public List<String> getTableColumns() {
		return this.tableMetaDataContext.getTableColumns();
	}
	
	public String createInsertString() {
		return this.tableMetaDataContext.createInsertString(getGeneratedKeyNames());
	}
	
	public int[] createInsertTypes() {
		return this.tableMetaDataContext.createInsertTypes();
	}
	
	public List<Object> matchInParameterValuesWithInsertColumns(SqlParameterSource sqlParameterSource) {
		return this.tableMetaDataContext.matchInParameterValuesWithInsertColumns(sqlParameterSource);
	}

	public String createUpdateString(String[] whereColumns) {
		Set<String> whereColumnSet = new HashSet<String>(whereColumns.length);
		for (String whereColumn : whereColumns) {
			whereColumnSet.add(whereColumn);
		}
		//
		StringBuilder updateStatement = new StringBuilder();
		updateStatement.append("UPDATE ");
		if (this.getSchemaName() != null) {
			updateStatement.append(getSchemaName());
			updateStatement.append(".");
		}
		updateStatement.append(getTableName());
		updateStatement.append(" SET ");
		//
		int columnCount = 0;
		for(String column : getTableColumns()) {
			if(!whereColumnSet.contains(column)) {
				columnCount++;
				if (columnCount > 1) {
					updateStatement.append(", ");
				}
				updateStatement.append(column).append(" = ?");
			}
		}
		//
		if(!whereColumnSet.isEmpty()) {
			updateStatement.append(" WHERE ");
			columnCount = 0;
			for(String whereColumn : whereColumns) {
				columnCount++;
				if (columnCount > 1) {
					updateStatement.append(" AND ");
				}
				updateStatement.append(whereColumn).append(" = ?");
			}
		}
		return updateStatement.toString();
	}
	
	public int[] createUpdateTypes(String[] whereColumnNames) {
		List<String> columns = new ArrayList<String>(getTableColumns());
		for(String whereColumnName : whereColumnNames) {
			columns.add(whereColumnName);
		}
		//
		return createTypes(columns);
	}
	
	public List<Object> matchInParameterValuesWithUpdateColumns(
			SqlParameterSource parameterSource, String[] whereColumnNames) {
		List<String> columns = new ArrayList<String>(getTableColumns());
		for(String whereColumnName : whereColumnNames) {
			columns.add(whereColumnName);
		}
		//
		return matchInParameterValuesWithColumns(parameterSource, columns);
	}
	
	public String createDeleteString(String[] whereColumns) {
		Set<String> whereColumnSet = new HashSet<String>(whereColumns.length);
		for (String whereColumn : whereColumns) {
			whereColumnSet.add(whereColumn);
		}
		//
		StringBuilder deleteStatement = new StringBuilder();
		deleteStatement.append("DELETE FROM ");
		if (this.getSchemaName() != null) {
			deleteStatement.append(getSchemaName());
			deleteStatement.append(".");
		}
		deleteStatement.append(getTableName());
		//
		if(!whereColumnSet.isEmpty()) {
			deleteStatement.append(" WHERE ");
			int columnCount = 0;
			for(String whereColumn : whereColumns) {
				columnCount++;
				if (columnCount > 1) {
					deleteStatement.append(" AND ");
				}
				deleteStatement.append(whereColumn).append(" = ?");
			}
		}
		return deleteStatement.toString();
	}
	
	public int[] createDeleteTypes(String[] whereColumnNames) {
		List<String> columns = new ArrayList<String>();
		for(String whereColumnName : whereColumnNames) {
			columns.add(whereColumnName);
		}
		//
		return createTypes(columns);
	}
	
	public List<Object> matchInParameterValuesWithDeleteColumns(
			SqlParameterSource parameterSource, String[] whereColumnNames) {
		List<String> columns = new ArrayList<String>();
		for(String whereColumnName : whereColumnNames) {
			columns.add(whereColumnName);
		}
		//
		return matchInParameterValuesWithColumns(parameterSource, columns);
	}
	
	public String createSelectString(String[] whereColumns) {
		whereColumns = whereColumns==null?new String[0]:whereColumns;
		//
		Set<String> whereColumnSet = new HashSet<String>(whereColumns.length);
		for (String whereColumn : whereColumns) {
			whereColumnSet.add(whereColumn);
		}
		//
		StringBuilder selectStatement = new StringBuilder();
		selectStatement.append("SELECT * FROM ");
		if (this.getSchemaName() != null) {
			selectStatement.append(getSchemaName());
			selectStatement.append(".");
		}
		selectStatement.append(getTableName());
		//
		if(!whereColumnSet.isEmpty()) {
			selectStatement.append(" WHERE ");
			//
			int columnCount = 0;
			for(String whereColumn : whereColumns) {
				columnCount++;
				if (columnCount > 1) {
					selectStatement.append(" AND ");
				}
				selectStatement.append(whereColumn).append(" = ?");
			}
		}
		return selectStatement.toString();
	}
	
	public int[] createSelectTypes(String[] whereColumnNames) {
		List<String> columns = new ArrayList<String>(getTableColumns());
		for(String whereColumnName : whereColumnNames) {
			columns.add(whereColumnName);
		}
		//
		return createTypes(columns);
	}
	
	public List<Object> matchInParameterValuesWithSelectColumns(
			SqlParameterSource parameterSource, String[] whereColumnNames) {
		List<String> columns = new ArrayList<String>(getTableColumns());
		for(String whereColumnName : whereColumnNames) {
			columns.add(whereColumnName);
		}
		//
		return matchInParameterValuesWithColumns(parameterSource, columns);
	}
	
	private int[] createTypes(List<String> columns) {
		int[] types = new int[columns.size()];
		List<TableParameterMetaData> parameters = this.metaDataProvider.getTableParameterMetaData();
		Map<String, TableParameterMetaData> parameterMap = new HashMap<String, TableParameterMetaData>(parameters.size());
		for (TableParameterMetaData tpmd : parameters) {
			parameterMap.put(tpmd.getParameterName().toUpperCase(), tpmd);
		}
		int typeIndx = 0;
		for (String column : columns) {
			if (column == null) {
				types[typeIndx] = SqlTypeValue.TYPE_UNKNOWN;
			}
			else {
				TableParameterMetaData tpmd = parameterMap.get(column.toUpperCase());
				if (tpmd != null) {
					types[typeIndx] = tpmd.getSqlType();
				}
				else {
					types[typeIndx] = SqlTypeValue.TYPE_UNKNOWN;
				}
			}
			typeIndx++;
		}
		return types;
	}
	
	@SuppressWarnings("rawtypes")
	private List<Object> matchInParameterValuesWithColumns(SqlParameterSource parameterSource, List<String> columns) {
		List<Object> values = new ArrayList<Object>();
		// for parameter source lookups we need to provide caseinsensitive lookup support since the
		// database metadata is not necessarily providing case sensitive column names
		Map caseInsensitiveParameterNames =
				SqlParameterSourceUtils.extractCaseInsensitiveParameterNames(parameterSource);
		for (String column : columns) {
			if (parameterSource.hasValue(column)) {
				values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, column));
			}
			else {
				String lowerCaseName = column.toLowerCase();
				if (parameterSource.hasValue(lowerCaseName)) {
					values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, lowerCaseName));
				}
				else {
					String propertyName = JdbcUtils.convertUnderscoreNameToPropertyName(column);
					if (parameterSource.hasValue(propertyName)) {
						values.add(SqlParameterSourceUtils.getTypedValue(parameterSource, propertyName));
					}
					else {
						if (caseInsensitiveParameterNames.containsKey(lowerCaseName)) {
							values.add(
									SqlParameterSourceUtils.getTypedValue(parameterSource,
											(String) caseInsensitiveParameterNames.get(lowerCaseName)));
						}
						else {
							values.add(null);
						}
					}
				}
			}
		}
		return values;
	}

}
