package org.sarons.spring4me.jdbc.procedure;


import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.object.StoredProcedure;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:24
 */
public abstract class GenericProcedureTemplate extends StoredProcedure {
	
	protected abstract String getProcedureName();
	
	protected abstract void declareParameters();
	
	public void afterPropertiesSet() {
		setSql(getSqlString());
		//
		declareParameters();
		//
		super.afterPropertiesSet();
	}

	static final String QUERY_TACODE = "select c_tano from ttainfo where c_flag = '1'";
	static final String QUERY_PROCEDURE = "select procedure_name from user_procedures where object_name = ? and procedure_name = ?";
	private String getSqlString() {
		String tacode = (String) getJdbcTemplate().queryForObject(QUERY_TACODE, String.class);
		String packageName = StringUtils.substringBefore(getProcedureName(), ".");
		String procedureName = StringUtils.substringAfter(getProcedureName(), ".") + "_" + tacode;
		try {
			return packageName + "." + (String) getJdbcTemplate().queryForObject(QUERY_PROCEDURE, 
					new Object[]{packageName.toUpperCase(), procedureName.toUpperCase()}, String.class);
		} catch (Exception e) {
			return getProcedureName();
		}
	}
	
}
