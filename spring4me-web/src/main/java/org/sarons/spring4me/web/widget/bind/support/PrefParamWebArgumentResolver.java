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

package org.sarons.spring4me.web.widget.bind.support;

import org.sarons.spring4me.web.widget.bind.annotation.PrefParam;
import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.sarons.spring4me.web.widget.http.HttpWidgetRequest;
import org.sarons.spring4me.web.widget.utils.WidgetConfigUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:26:12 AM
 */
public class PrefParamWebArgumentResolver implements WebArgumentResolver  {

	private ConversionService conversionService;

    public PrefParamWebArgumentResolver() {
    }
    
    public PrefParamWebArgumentResolver(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    
    public Object resolveArgument(MethodParameter methodParameter, 
            NativeWebRequest webRequest) throws Exception {
        PrefParam prefParam = methodParameter.getParameterAnnotation(PrefParam.class);
        if(prefParam==null) {
            return WebArgumentResolver.UNRESOLVED;
        }
        //
        Object value = getValue(webRequest, methodParameter.getParameterName());
        if(value==null) {
            value = prefParam.value();
        }
        //
        Class<?> requiredType = methodParameter.getParameterType();
        if(conversionService.canConvert(String.class, requiredType)) {
            return conversionService.convert(value, TypeDescriptor.valueOf(String.class), 
                    TypeDescriptor.valueOf(requiredType));
        }
        return WebArgumentResolver.UNRESOLVED;
    }
    
    private String getValue(NativeWebRequest nativeRequest, String key) {
        HttpWidgetRequest widgetRequest = nativeRequest.getNativeRequest(HttpWidgetRequest.class);
        WidgetConfig widgetConfig = WidgetConfigUtils.getWidgetConfig(widgetRequest);
        return widgetConfig.getPreferences().get(key);
    }
    
}
