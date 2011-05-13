package org.osforce.spring4me.web.bind.support;

import org.osforce.spring4me.web.bind.annotation.Pref;
import org.osforce.spring4me.web.widget.WidgetConfig;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1
 * @create May 13, 2011 - 4:37:04 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class PrefAnnotationArgumentResolver implements WebArgumentResolver {

	private ConversionService conversionService;

	public PrefAnnotationArgumentResolver() {
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	public Object resolveArgument(MethodParameter methodParameter,
			NativeWebRequest webRequest) throws Exception {
		Pref prefType = methodParameter.getParameterAnnotation(Pref.class);
		if(prefType!=null) {
			Object value = resolvePrefValue(methodParameter, webRequest);
			return value!=null ? value : prefType.value();
		}
		return WebArgumentResolver.UNRESOLVED;
	}

	protected Object resolvePrefValue(MethodParameter methodParameter,
			NativeWebRequest webRequest) {
		String paramName = methodParameter.getParameterName();
		WidgetConfig widgetConfig = (WidgetConfig) webRequest
				.getAttribute(WidgetConfig.KEY, WebRequest.SCOPE_REQUEST);
		Class<?> targetType = methodParameter.getParameterType();
		if(conversionService.canConvert(String.class, targetType)) {
			String source = widgetConfig.getPrefs().get(paramName);
			return conversionService.convert(source, targetType);
		}
		return null;
	}

}
