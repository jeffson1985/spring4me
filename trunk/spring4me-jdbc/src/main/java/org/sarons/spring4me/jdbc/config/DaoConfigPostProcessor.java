package org.sarons.spring4me.jdbc.config;

import javax.sql.DataSource;

import org.sarons.spring4me.jdbc.interceptor.DaoMethodInterceptor;
import org.sarons.spring4me.jdbc.pagination.SqlGenerator;
import org.sarons.spring4me.jdbc.sql.SqlTemplateParser;
import org.sarons.spring4me.jdbc.sql.VelocitySqlTemplateParser;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:54:10
 */
public class DaoConfigPostProcessor implements ResourceLoaderAware, 
	BeanFactoryPostProcessor {
	
	private static final String DAO_CONFIG_LOCATION_PATTERN = "classpath*:/**/*Dao.xml";

	private DataSource dataSource;
	
	private SqlGenerator sqlGenerator;
	
	private ResourceLoader resourceLoader;
	
	private SqlTemplateParser sqlTemplateParser = new VelocitySqlTemplateParser();
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public void setSqlGenerator(SqlGenerator sqlGenerator) {
		this.sqlGenerator = sqlGenerator;
	}
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public void setSqlTemplateParser(SqlTemplateParser sqlTemplateParser) {
		this.sqlTemplateParser = sqlTemplateParser;
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
			//
			ResourcePatternResolver rpr = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
			Resource[] daoConfigResources = rpr.getResources(DAO_CONFIG_LOCATION_PATTERN);
			for(Resource daoConfigResource : daoConfigResources) {
				DaoConfig daoConfig = DaoConfigParser.parse(daoConfigResource);
				MapperConfig mapperConfig = findAndParse(daoConfig.getMapper());
				daoConfig.setMapperConfig(mapperConfig);
				//
				DaoMethodInterceptor interceptor = new DaoMethodInterceptor();
				interceptor.initialize(daoConfig, jdbcTemplate, sqlGenerator, sqlTemplateParser);
				//
				String daoName = getDaoName(daoConfig.getDaoClass());
				Object daoInstance = ProxyFactory.getProxy(daoConfig.getDaoClass(), interceptor);
				beanFactory.registerSingleton(daoName, daoInstance);
			}
		} catch (Exception e) {
			throw new FatalBeanException(e.getMessage(), e);
		}
	}

	protected MapperConfig findAndParse(String mapper) throws Exception {
		ResourcePatternResolver rpr = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		Resource[] mapperConfigResources = rpr.getResources("classpath*:/**/"+mapper+".xml");
		//
		Assert.isTrue(mapperConfigResources!=null && mapperConfigResources.length==1, 
				mapper + ".xml can not be found in classpath!");
		//
		return MapperConfigParser.parse(mapperConfigResources[0]);
	}

	protected String getDaoName(Class<?> daoClass) {
		return StringUtils.uncapitalize(daoClass.getSimpleName());
	}

}
