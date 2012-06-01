package org.osforce.spring4me.web.navigation.utils;

import javax.servlet.http.HttpServletRequest;

import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.navigation.config.NavigationConfig;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午11:41:31
 */
public abstract class NavigationConfigUtils {

	public static NavigationConfig getFromNavigationConfig(HttpServletRequest request) {
		return (NavigationConfig) request.getSession().getAttribute(Keys.REQUEST_KEY_FROM_NAVIGATION_CONFIG);
	}
	
	public static void setFromNavigationConfig(HttpServletRequest request, NavigationConfig navigationConfig) {
		request.getSession().setAttribute(Keys.REQUEST_KEY_FROM_NAVIGATION_CONFIG, navigationConfig);
	}
	
	public static NavigationConfig getToNavigationConfig(HttpServletRequest request) {
		return (NavigationConfig) request.getSession().getAttribute(Keys.REQUEST_KEY_TO_NAVIGATION_CONFIG);
	}
	
	public static void setToNavigationConfig(HttpServletRequest request, NavigationConfig navigationConfig) {
		request.getSession().setAttribute(Keys.REQUEST_KEY_TO_NAVIGATION_CONFIG, navigationConfig);
	}
	
}
