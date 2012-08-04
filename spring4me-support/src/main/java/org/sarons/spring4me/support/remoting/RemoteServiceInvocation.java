package org.sarons.spring4me.support.remoting;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:01:06
 */
public class RemoteServiceInvocation {

	private String method;
	private Object[] arguments;
	private Object returnValue;

	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
}
