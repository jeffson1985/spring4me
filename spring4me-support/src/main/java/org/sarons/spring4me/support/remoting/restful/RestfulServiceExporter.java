package org.sarons.spring4me.support.remoting.restful;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.sarons.spring4me.support.remoting.RemoteServiceInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteExporter;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.util.NestedServletException;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:56
 */
public class RestfulServiceExporter extends RemoteExporter 
	implements HttpRequestHandler, InitializingBean {
	
	private Object proxyService;
	
	private ObjectMapper objectMapper = new ObjectMapper().enableDefaultTyping();
	
	private static final Map<String, Method> methodCache = new HashMap<String, Method>();

	public void afterPropertiesSet() throws Exception {
		this.proxyService = getProxyForService();
	}

	public Object getProxyService() {
		return proxyService;
	}
	
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			RemoteServiceInvocation invocation = objectMapper.readValue(request.getInputStream(), RemoteServiceInvocation.class);
			//
			Method method = findTargetMethod(getServiceInterface(), invocation.getMethod());
			//
			Object result = ReflectionUtils.invokeMethod(method, getProxyService(), invocation.getArguments());
			invocation.setReturnValue(result);
			//
			writeToResponse(request, response, invocation);
		} catch (Exception e) {
			throw new NestedServletException(e.getMessage(), e);
		}
	}
	
	private void writeToResponse(HttpServletRequest request, HttpServletResponse response, 
			RemoteServiceInvocation invocation) throws JsonGenerationException, JsonMappingException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		//
		objectMapper.writeValue(response.getWriter(), invocation);
	}
	
	private Method findTargetMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
		Assert.notNull(clazz);
		Assert.notNull(methodName);
		//
		String methodKey = clazz.getName() + "." + methodName; 
		Method targetMethod = methodCache.get(methodKey);
		if(targetMethod==null) {
			synchronized (methodCache) {
				if(targetMethod==null) {
					Method[] methods = clazz.getDeclaredMethods();
					for(Method method : methods) {
						if(methodName.equals(method.getName())) {
							targetMethod = method;
							break;
						}
					}
					//
					if(targetMethod==null) {
						throw new NoSuchMethodException("Method " + methodName + " not decleared in class " + clazz);
					}
					methodCache.put(methodKey, targetMethod);
				}
			}
		}
		//
		return targetMethod;
	}
	
}
