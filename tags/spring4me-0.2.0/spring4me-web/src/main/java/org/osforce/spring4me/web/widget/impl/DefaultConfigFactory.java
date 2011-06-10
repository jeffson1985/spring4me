package org.osforce.spring4me.web.widget.impl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.widget.ConfigFactory;
import org.osforce.spring4me.web.widget.ConfigParser;
import org.osforce.spring4me.web.widget.PageConfig;
import org.osforce.spring4me.web.widget.WidgetConfig;
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

	private Map<String, PageConfig> simpleCache = new HashMap<String, PageConfig>();
	
	private String prefix = "/WEB-INF/pages";
	private String suffix = ".xml";

	private ConfigParser configParser;
	private ResourceLoader resourceLoader;
	private Boolean cache = true;
	
	public DefaultConfigFactory() {
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
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

	protected String getPath(String path, String locale) {
		String retPath = StringUtils.substringBefore(path, "?");
		if(StringUtils.isNotBlank(locale)) {
			retPath += "_" + locale;
		}
		if(path.startsWith("/")) {
			retPath = prefix + retPath + suffix;
		} else {
			retPath = prefix + "/" + retPath + suffix;
		}
		return retPath;
	}
	
	protected Map<String, String> getParamMap(String path) {
		Map<String, String> paramMap = new HashMap<String, String>();
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

}
