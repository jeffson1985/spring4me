/*
 * Copyright 2011-2012 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osforce.spring4me.web.widget.bind.support;

import org.osforce.spring4me.web.widget.bind.annotation.PrefParam;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-3-19 - 下午8:51:36
 */
public class PrefParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
	
	private ConversionService conversionService;
	
	public PrefParamMethodArgumentResolver() {
	}
	
	public PrefParamMethodArgumentResolver(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}
	
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(PrefParam.class);
	}

	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		PrefParam prefParam = parameter.getParameterAnnotation(PrefParam.class);
		String value = getValue(webRequest, parameter.getParameterName());
		if(value==null) {
			value = prefParam.value();
		}
		//
		Class<?> requiredType = parameter.getParameterType();
        if(conversionService.canConvert(String.class, requiredType)) {
            return conversionService.convert(value, TypeDescriptor.valueOf(String.class), 
                    TypeDescriptor.valueOf(requiredType));
        }
		//
		return null;
	}
	
	private String getValue(NativeWebRequest nativeRequest, String key) {
        HttpWidgetRequest widgetRequest = nativeRequest.getNativeRequest(HttpWidgetRequest.class);
        WidgetConfig widgetConfig = widgetRequest.getWidgetConfig();
        return widgetConfig.getPreferences().get(key);
    }

}
