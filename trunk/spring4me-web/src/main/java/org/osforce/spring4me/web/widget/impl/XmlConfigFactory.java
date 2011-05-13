package org.osforce.spring4me.web.widget.impl;

import java.io.File;
import java.util.Locale;

import org.osforce.spring4me.web.widget.ConfigFactory;
import org.osforce.spring4me.web.widget.ConfigParser;
import org.osforce.spring4me.web.widget.PageConfig;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1
 * @create May 13, 2011 - 4:38:02 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class XmlConfigFactory implements ConfigFactory, ResourceLoaderAware {

	private String prefix = "/WEB-INF/pages";
	private String suffix = ".xml";

	private ResourceLoader resourceLoader;

	public XmlConfigFactory() {
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}


	public PageConfig getPageConfig(String path, Locale locale) throws Exception {
		String pathLocale = getPath(path + "_" + locale.toString());
		Resource resource = resourceLoader.getResource(pathLocale);
		File xmlPage = null;
		if(resource.exists()) {
			xmlPage = resource.getFile();
		}
		resource = resourceLoader.getResource(getPath(path));
		if(resource.exists()) {
			xmlPage = resource.getFile();
		}
		return ConfigParser.parse(xmlPage);
	}

	protected String getPath(String path) {
		String retPath = null;
		if(path.startsWith("/")) {
			retPath = prefix + path + suffix;
		} else {
			retPath = prefix + "/" + path + suffix;
		}
		return retPath;
	}

}
