package org.osforce.spring4me.web.bind.support;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.bind.annotation.PrefParam;
import org.osforce.spring4me.web.bind.annotation.RequestAttr;
import org.osforce.spring4me.web.bind.annotation.SessionAttr;
import org.osforce.spring4me.web.bind.annotation.SessionParam;
import org.osforce.spring4me.web.widget.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:37:04 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class WidgetCustomArgumentResolver implements WebArgumentResolver {

	private ConversionService conversionService;

	public WidgetCustomArgumentResolver() {
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		PrefParam prefParamType = methodParameter.getParameterAnnotation(PrefParam.class);
		RequestAttr requestAttrType = methodParameter.getParameterAnnotation(RequestAttr.class);
		SessionAttr sessionAttrType = methodParameter.getParameterAnnotation(SessionAttr.class);
		SessionParam sessionParamType = methodParameter.getParameterAnnotation(SessionParam.class);
		if(prefParamType!=null) {
			return resolvePrefValue(methodParameter, webRequest, prefParamType);
		} else if(requestAttrType!=null) {
			return resolveRequestValue(methodParameter, webRequest, requestAttrType);
		} else if(sessionAttrType!=null) {
			return resolveSessionValue(methodParameter, webRequest, sessionAttrType);
		} else if(sessionParamType!=null){
			return resolveSessionValue(methodParameter, webRequest, sessionParamType);
		}else if(methodParameter.getParameterType().isAssignableFrom(WidgetConfig.class)) {
			return getWidgetConfig(webRequest);
		} else if(methodParameter.getParameterType().isAssignableFrom(PageConfig.class)) {
			return getPageConfig(webRequest);
		}
		return WebArgumentResolver.UNRESOLVED;
	}

	protected Object resolvePrefValue(MethodParameter methodParameter,
			NativeWebRequest webRequest, PrefParam prefParam) {
		String paramName = methodParameter.getParameterName();
		//
		Class<?> targetType = methodParameter.getParameterType();
		if(conversionService.canConvert(String.class, targetType)) {
			String source = getWidgetConfig(webRequest).getPrefs().get(paramName);
			source = StringUtils.isNotBlank(source) ? source : prefParam.value();
			if(prefParam.required()) {
				Assert.hasText(source);
			}
			return conversionService.convert(source, targetType);
		}
		return null;
	}
	
	protected Object resolveRequestValue(MethodParameter methodParameter,
			NativeWebRequest webRequest, RequestAttr requestAttr) {
		String attrName = methodParameter.getParameterName();
		if(StringUtils.isNotBlank(requestAttr.value())) {
			attrName = requestAttr.value();
		}
		return webRequest.getAttribute(attrName, WebRequest.SCOPE_REQUEST);
	}
	
	protected Object resolveSessionValue(MethodParameter methodParameter,
			NativeWebRequest webRequest, SessionAttr sessionAttr) {
		String attrName = methodParameter.getParameterName();
		if(StringUtils.isNotBlank(sessionAttr.value())) {
			attrName = sessionAttr.value();
		}
		return webRequest.getAttribute(attrName, WebRequest.SCOPE_SESSION);
	}
	
	protected Object resolveSessionValue(MethodParameter methodParameter,
			NativeWebRequest webRequest, SessionParam sessionParam) {
		String paramName = methodParameter.getParameterName();
		if(StringUtils.isNotBlank(sessionParam.value())) {
			paramName = sessionParam.value();
		}
		//
		Object value = null;
		String source = webRequest.getParameter(paramName);
		Class<?> targetType = methodParameter.getParameterType();
		if(conversionService.canConvert(String.class, targetType)) {
			if(StringUtils.isNotBlank(source)) {
				value = conversionService.convert(source, targetType);
			}
		}
		//
		if(value==null) {
			value = webRequest.getAttribute(paramName, WebRequest.SCOPE_SESSION);
		}
		//
		if(value!=null) {
			webRequest.setAttribute(paramName, value, WebRequest.SCOPE_SESSION);
		}
		return value;
	}
	
	private WidgetConfig getWidgetConfig(WebRequest webRequest) {
		return (WidgetConfig) webRequest
				.getAttribute(WidgetConfig.KEY, WebRequest.SCOPE_REQUEST); 
	}
	
	private PageConfig getPageConfig(WebRequest webRequest) {
		return (PageConfig) webRequest
				.getAttribute(PageConfig.KEY, WebRequest.SCOPE_REQUEST);
	}

}
