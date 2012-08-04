package org.sarons.spring4me.jdbc.pagination;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:13
 */
public interface SqlGenerator {

	String countSql(String selectSql);

	String paginationSql(Page<?> page, String selectSql);
	
}
