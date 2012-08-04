package org.sarons.spring4me.jdbc.pagination.impl;

import org.sarons.spring4me.jdbc.pagination.AbstractSqlGenerator;
import org.sarons.spring4me.jdbc.pagination.Page;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:49
 */
public class HsqlSqlGenerator extends AbstractSqlGenerator {

	public String paginationSql(Page<?> page, String selectSql) {
		StringBuilder sqlBuilder = new StringBuilder(selectSql);
		sqlBuilder.append(" limit ").append(page.getPageSize());
		sqlBuilder.append(" offset ").append(page.getFirst());
		return sqlBuilder.toString();
	}

}
