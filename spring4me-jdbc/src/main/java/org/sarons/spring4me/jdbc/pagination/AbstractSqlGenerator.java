package org.sarons.spring4me.jdbc.pagination;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:03
 */
public abstract class AbstractSqlGenerator implements SqlGenerator {

	public String countSql(String selectSql) {
		StringBuilder sqlBuilder = new StringBuilder();
		//
		sqlBuilder.append("select count(1) ");
		sqlBuilder.append(selectSql.subSequence(selectSql.indexOf("from"), selectSql.length()));
		//
		return sqlBuilder.toString();
	}

}
