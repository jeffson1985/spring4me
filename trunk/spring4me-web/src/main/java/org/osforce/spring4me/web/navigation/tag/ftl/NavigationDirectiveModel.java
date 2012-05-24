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

package org.osforce.spring4me.web.navigation.tag.ftl;

import java.io.IOException;
import java.util.Map;

import org.osforce.spring4me.web.navigation.tag.NavigationProcessor;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.springframework.util.Assert;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * for link
 * <@navigation event="view-profile" />
 * for form action
 * <@navigation event="view-profile" action="${contextPath}/profile/update" />
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-9 - 下午1:45:39
 */
public class NavigationDirectiveModel implements TemplateDirectiveModel {
	
	@SuppressWarnings("rawtypes")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String event = params.get("event").toString();
		String action = params.get("action").toString();
		//
		Assert.notNull("Argument event can not be null!", event);
		//
		BeanModel pageConfigBeanModel = (BeanModel) env.getDataModel().get(PageConfig.KEY);
		PageConfig pageConfig = (PageConfig) pageConfigBeanModel.getWrappedObject();
		//
		String eventDrivenServiceUrl = ((SimpleScalar)env.getDataModel().get("eventDrivenServiceUrl")).getAsString();
		//
		NavigationProcessor processor = new NavigationProcessor(pageConfig, eventDrivenServiceUrl);
		processor.process(env.getOut(), event, action);
	}

}
