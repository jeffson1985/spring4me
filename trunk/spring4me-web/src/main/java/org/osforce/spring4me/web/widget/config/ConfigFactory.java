package org.osforce.spring4me.web.widget.config;

import java.util.Locale;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:07 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface ConfigFactory {

	static final String KEY = ConfigFactory.class.getName();

	/**
	 * Get page config  by ConfigFactory
	 * @param path pattern /home?qualifier=people
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	PageConfig getPageConfig(String path, Locale locale) throws Exception;

}
