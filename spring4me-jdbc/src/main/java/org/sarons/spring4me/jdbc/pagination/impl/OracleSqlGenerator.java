package org.sarons.spring4me.jdbc.pagination.impl;

import org.sarons.spring4me.jdbc.pagination.AbstractSqlGenerator;
import org.sarons.spring4me.jdbc.pagination.Page;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:57
 */
public class OracleSqlGenerator extends AbstractSqlGenerator {

	public String paginationSql(Page<?> page, String selectSql) {
		StringBuilder sqlBuilder = new StringBuilder(selectSql);
		sqlBuilder.insert(0, "SELECT * FROM (SELECT a.*, rownum r__ FROM (");
		sqlBuilder.append(") a WHERE rownum < ");
		sqlBuilder.append(page.getFirst() + page.getPageSize());
		sqlBuilder.append(") WHERE r__ >= ");
		sqlBuilder.append(page.getFirst());
		return sqlBuilder.toString();
	}

}
