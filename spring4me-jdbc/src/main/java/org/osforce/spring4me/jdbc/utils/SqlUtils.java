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

package org.osforce.spring4me.jdbc.utils;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 18, 2012 - 10:49:25 AM
 */
public abstract class SqlUtils {

	public static String getCountSql(String sql) {
		String sqlCopy = cleanSql(sql).toLowerCase();
		sqlCopy = sqlCopy.substring(sqlCopy.indexOf("from"));
		return "select count(*) " + sqlCopy;
	}
	
	public static String cleanSql(String sql) {
		StringBuilder sqlBuilder = new StringBuilder();
		//
		char tmp = Character.MIN_VALUE;
		for(char c : sql.trim().toCharArray()) {
			if(c==' ' && tmp==' ') {
				continue;
			}
			//
			sqlBuilder.append(c);
			//
			tmp = c;
		}
		return sqlBuilder.toString();
	}
	
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	public static String name(String underscoreName) {
		StringBuilder result = new StringBuilder();
		if(underscoreName!=null && underscoreName.length()>0) {
			for(int i=0; i<underscoreName.length(); i++) {
				if(underscoreName.charAt(i)=='_') {
					i +=1;
					result.append(Character.toUpperCase(underscoreName.charAt(i)));
				} else {
					result.append(underscoreName.charAt(i));
				}
			}
		}
		//
		return result.toString();
	}
	
	public static String[] getWhereColumnNames(String[] generatedKeyNames, String[] filterFields) {
		String[] whereColumnNames = generatedKeyNames;
		if(filterFields!=null) {
			whereColumnNames = generateColumnNames(filterFields);
		}
		//
		return whereColumnNames;
	}

	private static String[] generateColumnNames(String[] fieldNames) {
		int length = fieldNames.length;
		String[] columnNames = new String[length];
		for(int i=0; i<length; i++) {
			String fieldName = fieldNames[i];
			columnNames[i] = SqlUtils.underscoreName(fieldName);
		}
		//
		return columnNames;
	}
	
}
