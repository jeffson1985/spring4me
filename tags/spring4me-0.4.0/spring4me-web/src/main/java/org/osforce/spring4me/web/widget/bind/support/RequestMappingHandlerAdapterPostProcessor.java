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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * Spring 3.1 required
 * @author Gavin
 * @since 0.4.0
 * @create 2012-3-19 - 下午9:07:40
 */
public class RequestMappingHandlerAdapterPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

	private ConversionService conversionService = new DefaultFormattingConversionService();
    private List<HandlerMethodArgumentResolver> customArgumentResolvers = new ArrayList<HandlerMethodArgumentResolver>();
	
    public RequestMappingHandlerAdapterPostProcessor() {
	}
    
    public void setCustomArgumentResolver(HandlerMethodArgumentResolver customArgumentResolver) {
        this.customArgumentResolvers.add(customArgumentResolver);
    }

    public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers.addAll(customArgumentResolvers);
    }
    
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName)
    		throws BeansException {
    	if(bean instanceof RequestMappingHandlerAdapter) {
    		RequestMappingHandlerAdapter handlerAdapter = (RequestMappingHandlerAdapter) bean;
    		//
    		this.customArgumentResolvers.add(new WidgetConfigMethodArgumentResolver());
    		this.customArgumentResolvers.add(new PrefParamMethodArgumentResolver(conversionService));
    		//
    		handlerAdapter.setCustomArgumentResolvers(customArgumentResolvers);
    	}
    	return super.postProcessAfterInstantiation(bean, beanName);
    }
    
}
