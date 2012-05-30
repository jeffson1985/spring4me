package org.osforce.spring4me.web.flow;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午3:00:46
 */
public class FlowNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 4746225818670187068L;
	
	public FlowNotFoundException(String message) {
		super(message);
	}

	public FlowNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
