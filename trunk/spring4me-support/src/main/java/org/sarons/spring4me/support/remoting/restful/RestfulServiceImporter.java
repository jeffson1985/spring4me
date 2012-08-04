package org.sarons.spring4me.support.remoting.restful;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.sarons.spring4me.support.remoting.RemoteServiceInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.adapter.AdvisorAdapterRegistry;
import org.springframework.aop.framework.adapter.GlobalAdvisorAdapterRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.remoting.support.UrlBasedRemoteAccessor;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:01:02
 */
public class RestfulServiceImporter extends UrlBasedRemoteAccessor 
	implements MethodInterceptor, FactoryBean<Object> {
	
	private Object proxyService;
	
	private HttpClient httpClient;
	
	private ObjectMapper objectMapper;
	
	private Object[] interceptors = new Object[0];
	
	public void setInterceptors(Object[] interceptors) {
		this.interceptors = interceptors;
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		//
		this.proxyService = createProxyService();
		this.httpClient = createHttpClient();
		this.objectMapper = new ObjectMapper().enableDefaultTyping();
	}
	
	public Object getObject() throws Exception {
		return proxyService;
	}

	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName();
		if(isDeclaredMethod(getServiceInterface(), methodName)) {
			//
			HttpUriRequest request = createHttpUriRequest(invocation);
			HttpResponse response = httpClient.execute(request);
			//
			return extractData(response);
		}
		//
		throw new UnsupportedOperationException("Method " + methodName + " not support in proxy!");
	}
	
	protected Object createProxyService() {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.addInterface(getServiceInterface());
		//
		AdvisorAdapterRegistry adapterRegistry = GlobalAdvisorAdapterRegistry.getInstance();
		for(Object interceptor : interceptors) {
			proxyFactory.addAdvisor(adapterRegistry.wrap(interceptor));
		}
		//
		proxyFactory.addAdvice(this);
		return proxyFactory.getProxy(getBeanClassLoader());
	}
	
	protected HttpClient createHttpClient() {
		ClientConnectionManager connManager = new PoolingClientConnectionManager();
		HttpClient httpClient = new DefaultHttpClient(connManager);
		return httpClient;
	}
	
	protected HttpUriRequest createHttpUriRequest(MethodInvocation invocation) throws Throwable {
		HttpPost httpPost = new HttpPost(getServiceUrl());
		//
		RemoteServiceInvocation remoteInvocation = new RemoteServiceInvocation();
		remoteInvocation.setMethod(invocation.getMethod().getName());
		remoteInvocation.setArguments(invocation.getArguments());
		//
		byte[] bytes = objectMapper.writeValueAsBytes(remoteInvocation);
		HttpEntity httpEntity = new ByteArrayEntity(bytes);
		httpPost.setEntity(httpEntity);
		return httpPost;
	}
	
	protected Object extractData(HttpResponse response) throws Throwable {
		StatusLine statusLine = response.getStatusLine();
		if(statusLine.getStatusCode()==200) {
			RemoteServiceInvocation invocation =  objectMapper.readValue(
					response.getEntity().getContent(), RemoteServiceInvocation.class);
			return invocation.getReturnValue();
		}
		//
		String message = getServiceUrl() + " " + statusLine.getReasonPhrase();
		throw new RemoteAccessException(message);
	}
	
	private boolean isDeclaredMethod(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getMethods();
		for(Method method : methods) {
			if(methodName.equals(method.getName())) {
				return true;
			}
		}
		//
		return false;
	}
	
}
