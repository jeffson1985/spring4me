package org.osforce.spring4me.web.widget.config;

import java.io.File;
import java.util.Map;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 18, 2011 - 10:54:17 AM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public interface ConfigParser {

	static final String KEY = ConfigParser.class.getName();
	/**
	 * Parse page config from xml file by given params
	 * @param xmlPage
	 * @param paramMap Note: paramMap may be empty
	 * @return
	 */
	PageConfig parse(File xmlPage, Map<String, String> paramMap);
	
}
