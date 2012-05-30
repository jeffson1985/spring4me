package org.osforce.spring4me.web.navigation;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 下午1:11:52
 */
@SuppressWarnings("serial")
public class NavigationNotFoundException extends RuntimeException {

	public NavigationNotFoundException(String message) {
		super(message);
	}

	public NavigationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
