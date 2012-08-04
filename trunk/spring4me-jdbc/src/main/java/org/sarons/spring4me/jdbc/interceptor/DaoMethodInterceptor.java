package org.sarons.spring4me.jdbc.interceptor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.sarons.spring4me.jdbc.annotation.NamedSQL;
import org.sarons.spring4me.jdbc.annotation.SQLParam;
import org.sarons.spring4me.jdbc.config.DaoConfig;
import org.sarons.spring4me.jdbc.config.SqlConfig;
import org.sarons.spring4me.jdbc.mapper.ConfigurableBeanPropertyRowMapper;
import org.sarons.spring4me.jdbc.pagination.Page;
import org.sarons.spring4me.jdbc.pagination.SqlGenerator;
import org.sarons.spring4me.jdbc.sql.ParsedSql;
import org.sarons.spring4me.jdbc.sql.SqlTemplateParser;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ClassUtils;



/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:35
 */
public class DaoMethodInterceptor implements MethodInterceptor {
	
	private DaoConfig daoConfig;
	
	private RowMapper<?> rowMapper;
	
	private JdbcTemplate jdbcTemplate;
	
	private SqlTemplateParser sqlTemplateParser;
	
	private SqlGenerator sqlGenerator;
	
	public DaoConfig getDaoConfig() {
		return daoConfig;
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	public void initialize(DaoConfig daoConfig, JdbcTemplate jdbcTemplate,SqlGenerator sqlGenerator, SqlTemplateParser sqlTemplateParser) {
		this.daoConfig = daoConfig;
		this.jdbcTemplate = jdbcTemplate;
		this.sqlGenerator = sqlGenerator;
		this.sqlTemplateParser = sqlTemplateParser;
		this.rowMapper = new ConfigurableBeanPropertyRowMapper<Object>(daoConfig.getMapperConfig());
		//
		StringResourceRepository repository = StringResourceLoader.getRepository();
		for(String key : daoConfig.getSqlConfigMap().keySet()) {
			SqlConfig sqlConfig = daoConfig.getSqlConfig(key);
			String templateName = generateTemplateName(daoConfig, sqlConfig);
			repository.putStringResource(templateName, sqlConfig.getSql());
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//
		NamedSQL statement = AnnotationUtils.findAnnotation(invocation.getMethod(), NamedSQL.class);
		SqlConfig sqlConfig = daoConfig.getSqlConfig(statement.value());
		//
		Map<String, Object> model = getModel(invocation);
		//
		String templateName = generateTemplateName(daoConfig, sqlConfig);
		ParsedSql parsedSql = sqlTemplateParser.parse(templateName, model);
		Object[] args = resolveArguments(invocation, parsedSql, model);
		//
		Object result = null;
		String sql = parsedSql.getSql();
		if("select".equals(sqlConfig.getType())) {
			Class<?> mappedClass = daoConfig.getMapperConfig().getMappedClass();
			Class<?> returnClass = invocation.getMethod().getReturnType();
			if(ClassUtils.isAssignable(mappedClass, returnClass)) {
				result = getJdbcTemplate().queryForObject(sql, args, rowMapper);
			} 
			else if(ClassUtils.isAssignable(List.class, returnClass)) {
				result = getJdbcTemplate().query(sql, args, rowMapper);
			}
			else if(ClassUtils.isAssignable(Page.class, returnClass)) {
				Page<?> page = (Page<?>) invocation.getArguments()[0];
				if(page.isAutoCount() && page.getTotalCount()==Page.TOTAL_COUNT_UNKNOW) {
					String countSql = buildCountSql(sql);
					long totalCount = getJdbcTemplate().queryForLong(countSql, args);
					page.setTotalCount(totalCount);
				}
				//
				sql = wrapperPagination(page, sql);
				List list = getJdbcTemplate().query(sql, args, rowMapper);
				result = page.setResult(list);
			}
		} 
		else if("call".equals(sqlConfig.getType())) {
			/*CallableStatementCreator csc = new CallableStatementCreatorFactory(sql)
					.newCallableStatementCreator(new HashMap());
			List<SqlParameter> sqlParameters = declareSqlParameters(parsedSql);
			Map<String, Object> callResult = getJdbcTemplate().call(csc, sqlParameters);*/
			throw new UnsupportedOperationException("Not support procedure call yet!");
		}
		else {
			result = this.jdbcTemplate.update(sql, args);
		}
		//
		return result;
	}
	
	private String buildCountSql(String sql) {
		return sqlGenerator.countSql(sql);
	}
	
	private String wrapperPagination(Page<?> page, String sql) {
		return sqlGenerator.paginationSql(page, sql);
	}
	
	private Object[] resolveArguments(MethodInvocation invocation, 
			ParsedSql parsedSql, Map<String, Object> model) {
		List<Object> objectList = new ArrayList<Object>();
		Map<String, BeanWrapper> beanWrapperCache = new HashMap<String, BeanWrapper>();
		for(String placeholder : parsedSql.getPlaceholders()) {
			if(placeholder.contains(".")) {
				String objectName = placeholder.substring(0, placeholder.indexOf('.'));
				Object objectInstance = model.get(objectName);
				BeanWrapper beanWrapper = beanWrapperCache.get(objectName);
				if(beanWrapper==null && objectInstance!=null) {
					beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(objectInstance);
					beanWrapperCache.put(objectName, beanWrapper);
				}
				//
				String propertyName = getPropertyName(placeholder);
				Object propertyValue = beanWrapper==null ? null : beanWrapper.getPropertyValue(propertyName);
				objectList.add(propertyValue);
			} else {
				objectList.add(model.get(placeholder));
			}
		}
		return objectList.toArray();
	}
	
	private Map<String, Object> getModel(MethodInvocation invocation) {
		int index = 0;
		Map<String, Object> model = new HashMap<String, Object>();
		Annotation[][] parameterAnnotationArray = invocation.getMethod().getParameterAnnotations();
		for(Annotation[] array : parameterAnnotationArray) {
			for(Annotation annotation : array) {
				if(annotation instanceof SQLParam) {
					String name = ((SQLParam) annotation).value();
					Object value = invocation.getArguments()[index];
					model.put(name, value);
				}
			}
			//
			index++;
		}
		//
		return model;
	}
	
	private String generateTemplateName(DaoConfig daoConfig, SqlConfig sqlConfig) {
		return daoConfig.getDaoClass().getName() + "." + sqlConfig.getId();
	}
	
	private String getPropertyName(String placeholder) {
		return placeholder.substring(placeholder.indexOf('.')+1, placeholder.length());
	}
	
}
