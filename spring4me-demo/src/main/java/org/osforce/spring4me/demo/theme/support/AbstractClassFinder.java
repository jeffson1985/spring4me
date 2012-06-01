package org.osforce.spring4me.demo.theme.support;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

public abstract class AbstractClassFinder {
	
	private static final Log log = LogFactory.getLog(AbstractClassFinder.class);

	private String[] defaultPatterns = new String[0];
	
	private MetadataReaderFactory metadataReaderFacotory = new CachingMetadataReaderFactory();
	
	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	
	public AbstractClassFinder(String[] defaultPatterns) {
		this.defaultPatterns = defaultPatterns;
	}
	
	public Class<?> findOne(String pattern) {
		Set<Class<?>> targetClasses = find(pattern);
		if(targetClasses.isEmpty()) {
			return null;
		}
		//
		return targetClasses.toArray(new Class<?>[targetClasses.size()])[0];
	}
	
	public Set<Class<?>> find(String... patterns) {
		Set<Class<?>> targetClasses = new HashSet<Class<?>>();
		if(patterns!=null) {
			targetClasses = findByPatterns(patterns);
		}
		if(defaultPatterns!=null) {
			Set<Class<?>> defaultTargetClasses = findByPatterns(defaultPatterns);
			//
			targetClasses.addAll(defaultTargetClasses);
		}
		return targetClasses;
	}
	
	private Set<Class<?>> findByPatterns(String[] patterns) {
		Set<Class<?>> targetClasses = new HashSet<Class<?>>();
		//
		for(String pattern : patterns) {
			try {
				Resource[]  classResources = resourcePatternResolver.getResources(pattern);
				if(classResources==null) {
					continue;
				}
				//
				for(Resource classResource : classResources) {
					MetadataReader metadataReader = metadataReaderFacotory.getMetadataReader(classResource);
					String className = metadataReader.getClassMetadata().getClassName();
					Class<?> clazz = ClassUtils.forName(className, getClass().getClassLoader());
					targetClasses.add(clazz);
				}
				
			} catch (Exception e) {
				log.warn("Constraint not found by pattern " + pattern, e);
			} 
		}
		//
		return targetClasses;
	}
	
}
