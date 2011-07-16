package org.osforce.spring4me.web.widget.config.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.commons.collection.CollectionUtil;
import org.osforce.spring4me.web.widget.config.ConfigFactory;
import org.osforce.spring4me.web.widget.config.ConfigParser;
import org.osforce.spring4me.web.widget.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:02 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class DefaultConfigFactory implements ConfigFactory, ResourceLoaderAware {

	private Map<String, PageConfig> simpleCache = CollectionUtil.newHashMap();
	
	private String prefix = "/WEB-INF/pages/";
	private String sitePrefix = "/WEB-INF/sites/";
	private String SUFFIX = ".xml";

	private ConfigParser configParser;
	private ResourceLoader resourceLoader;
	private Boolean cache = true;
	
	public DefaultConfigFactory() {
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSitePrefix(String sitePrefix) {
		this.sitePrefix = sitePrefix;
	}
	
	public void setCache(Boolean cache) {
		this.cache = cache;
	}
	
	public void setConfigParser(ConfigParser configParser) {
		this.configParser = configParser;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public PageConfig getPageConfig(String path, Locale locale) throws Exception {
		String key = path + "T" + locale.toString();
		PageConfig pageConfig = simpleCache.get(key);
		if(cache && pageConfig!=null) {
			return pageConfig;
		}
		//
		File xmlPage = findPageFile(path, locale);
		Map<String, String> paramMap = getParamMap(path);
		pageConfig = configParser.parse(xmlPage, paramMap);
		if(StringUtils.isNotBlank(pageConfig.getParent())) {
			 xmlPage = findPageFile(pageConfig.getParent(), locale);
			 PageConfig parentPageConfig = configParser.parse(xmlPage, paramMap);
			 pageConfig = mergePageConfig(parentPageConfig, pageConfig);
		}
		//
		if(cache && pageConfig!=null) {
			simpleCache.put(key, pageConfig);
		}
		return pageConfig;
	}
	
	protected PageConfig mergePageConfig(PageConfig parent, PageConfig child) {
		Iterator<Entry<String, List<WidgetConfig>>> iter = child.getPlaceholders().entrySet().iterator();
		while(iter.hasNext()) {
			Entry<String, List<WidgetConfig>> entry = iter.next();
			parent.addPlaceholder(entry.getKey(), entry.getValue());
		}
		return parent;
	}
	
	protected File findPageFile(String path, Locale locale) throws IOException {
		String pathLocale = getPath(path, locale.toString());
		Resource resource = resourceLoader.getResource(pathLocale);
		File xmlPage = null;
		if(resource.exists()) {
			xmlPage = resource.getFile();
		}
		resource = resourceLoader.getResource(getPath(path, null));
		if(resource.exists()) {
			xmlPage = resource.getFile();
		}
		return xmlPage;
	}

	protected String getPath(String path, String locale) throws FileNotFoundException {
		Map<String, String> paramMap = getParamMap(path);
		String retPath = StringUtils.substringBefore(path, "?");
		StringBuffer pathBuffer = new StringBuffer();
		//
		if(paramMap.containsKey("domain")) {
			String domain = paramMap.get("domain");
			if(retPath.startsWith("/")) {
				pathBuffer.append(sitePrefix).append(domain)
						.append(retPath);
			} else {
				pathBuffer.append(sitePrefix).append(domain)
						.append("/").append(retPath);
			}
		} 
		//
		if(exist(pathBuffer, locale)) {
			return pathBuffer.toString();
		}
		//
		pathBuffer = new StringBuffer();
		if(retPath.startsWith("/")) {
			pathBuffer.append(prefix).append(retPath);
		} else {
			pathBuffer.append(prefix).append("/").append(retPath);
		}
		//
		if(exist(pathBuffer, locale)) {
			return pathBuffer.toString();
		}
		throw new FileNotFoundException(String.format("Page define file %s can not be found!", path));
	}
	
	protected Map<String, String> getParamMap(String path) {
		Map<String, String> paramMap = CollectionUtil.newHashMap();
		if(StringUtils.contains(path, "?")) {
			String paramStr = StringUtils.substringAfter(path, "?");
			String[] params = StringUtils.split(paramStr, "&");
			for(String param : params) {
				String paramName = StringUtils.substringBefore(param, "=");
				String paramValue = StringUtils.substringAfter(param, "=");
				paramMap.put(paramName, paramValue);
			}
		}
		return paramMap;
	}
	
	private Boolean exist(StringBuffer pathBuffer, String locale) {
		String path = pathBuffer.toString() + "_" + locale + SUFFIX;
		if(resourceLoader.getResource(path).exists()) {
			pathBuffer.append("_").append(locale).append(SUFFIX);
			return true;
		}
		//
		path = pathBuffer.toString() + SUFFIX;
		if(resourceLoader.getResource(path).exists()) {
			pathBuffer.append(SUFFIX);
			return true; 
		}
		return false;
	}

}
