package org.sarons.spring4me.support.remoting.metadata;

import java.lang.reflect.Method;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:46
 */
public class RemoteServiceMethodMetadata {

	private String method;
	
	private Class<?>[] argumentTypes;
	
	private Class<?> returnType;
	
	public RemoteServiceMethodMetadata() {
	}
	
	public RemoteServiceMethodMetadata(Method method) {
		this.method = method.getName();
		this.argumentTypes = method.getParameterTypes();
		this.returnType = method.getReturnType();
	}
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Class<?>[] getArgumentTypes() {
		return argumentTypes;
	}

	public void setArgumentTypes(Class<?>[] argumentTypes) {
		this.argumentTypes = argumentTypes;
	}
	
	public Class<?> getReturnType() {
		return returnType;
	}
	
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	
}
