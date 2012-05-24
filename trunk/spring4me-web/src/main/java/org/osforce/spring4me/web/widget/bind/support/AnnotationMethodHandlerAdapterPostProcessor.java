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
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

/***
 * Spring 3.0 required
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:25:59 AM
 */
public class AnnotationMethodHandlerAdapterPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

    private ConversionService conversionService = new FormattingConversionService();
    private List<WebArgumentResolver> customArgumentResolvers = new ArrayList<WebArgumentResolver>();

    public AnnotationMethodHandlerAdapterPostProcessor() {
    }

    public WebArgumentResolver[] getCustomArgumentResolvers() {
        return customArgumentResolvers.toArray(new WebArgumentResolver[customArgumentResolvers.size()]);
    }
    
    public void setCustomArgumentResolver(WebArgumentResolver customArgumentResolver) {
        this.customArgumentResolvers.add(customArgumentResolver);
    }

    public void setCustomArgumentResolvers(List<WebArgumentResolver> customArgumentResolvers) {
        this.customArgumentResolvers.addAll(customArgumentResolvers);
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof AnnotationMethodHandlerAdapter) {
            AnnotationMethodHandlerAdapter handlerAdapter = (AnnotationMethodHandlerAdapter) bean;
            //
            this.customArgumentResolvers.add(new WidgetConfigWebArgumentResolver());
            this.customArgumentResolvers.add(new PrefParamWebArgumentResolver(conversionService));
            //
            handlerAdapter.setCustomArgumentResolvers(getCustomArgumentResolvers());
        } 
        //
        return super.postProcessAfterInstantiation(bean, beanName);
    }
    
}
