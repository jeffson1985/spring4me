package org.osforce.spring4me.web.bind.support;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.bind.annotation.PrefParam;
import org.osforce.spring4me.web.widget.PageConfig;
import org.osforce.spring4me.web.widget.WidgetConfig;
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
		if(prefParamType!=null) {
			return resolvePrefValue(methodParameter, webRequest, prefParamType);
		} else if(methodParameter.getParameterType().isAssignableFrom(WidgetConfig.class)) {
			return getWidgetConfig(webRequest);
		} else if(methodParameter.getParameterType().isAssignableFrom(PageConfig.class)) {
			return getPageConfig(webRequest);
		}
		return WebArgumentResolver.UNRESOLVED;
	}

	protected Object resolvePrefValue(MethodParameter methodParameter,
			NativeWebRequest webRequest, PrefParam prefParam) {
		String paramName = methodParameter.getParameterName();
		WidgetConfig widgetConfig = (WidgetConfig) webRequest
				.getAttribute(WidgetConfig.KEY, WebRequest.SCOPE_REQUEST);
		
		Class<?> targetType = methodParameter.getParameterType();
		if(conversionService.canConvert(String.class, targetType)) {
			String source = widgetConfig.getPrefs().get(paramName);
			source = StringUtils.isNotBlank(source) ? source : prefParam.value();
			if(prefParam.required()) {
				Assert.hasText(source);
			}
			return conversionService.convert(source, targetType);
		}
		return WebArgumentResolver.UNRESOLVED;
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
