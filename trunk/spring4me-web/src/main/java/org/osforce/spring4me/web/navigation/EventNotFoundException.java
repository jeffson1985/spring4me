package org.osforce.spring4me.web.navigation;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-30 - 下午1:11:42
 */
@SuppressWarnings("serial")
public class EventNotFoundException extends RuntimeException {

	public EventNotFoundException(String message) {
		super(message);
	}
	
	public EventNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
