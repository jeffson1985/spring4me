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

package org.osforce.spring4me.web.page.tag.ftl;

import java.io.IOException;
import java.util.Map;

import org.osforce.spring4me.web.page.PlaceholderException;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.tag.PlaceholderCallback;
import org.osforce.spring4me.web.page.tag.PlaceholderProcessor;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidget;
import org.osforce.spring4me.web.widget.utils.WidgetUtils;
import org.springframework.util.Assert;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:31:41 AM
 */
public class PlaceholderDirectiveModel implements TemplateDirectiveModel {

	private static final String PARAM_GROUP_ID = "groupId";
	//
	private ThreadLocal<Environment> local = new ThreadLocal<Environment>();
	
	@SuppressWarnings("rawtypes")
	public void execute(final Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		local.set(env);
		//
		String groupId = null;
		//
		if(params.containsKey(PARAM_GROUP_ID)) {
			groupId = params.get(PARAM_GROUP_ID).toString();
		}
		//
		Assert.notNull(groupId, "Argument groupId can not be null!");
		//
		BeanModel pageConfigBeanModel = (BeanModel) env.getDataModel().get(PageConfig.KEY);
		PageConfig pageConfig = (PageConfig) pageConfigBeanModel.getWrappedObject();
		//
		PlaceholderProcessor pp = new PlaceholderProcessor(pageConfig);
		pp.setCallback(new PlaceholderCallback(){

			public HttpWidget getHttpWidget(WidgetConfig widgetConfig) throws Exception {
				String httpWidgetKey = WidgetUtils.generateHttpWidgetKey(widgetConfig);
				BeanModel httpWidgetBeanModel = (BeanModel) env.getDataModel().get(httpWidgetKey);
				HttpWidget httpWidget = null;
				if(httpWidgetBeanModel!=null) {
					httpWidget = (HttpWidget) httpWidgetBeanModel.getWrappedObject();
				}
				return httpWidget;
			}
			
		});
		//
		try {
			pp.process(env.getOut(), groupId);
		} catch (Exception e) {
			throw new PlaceholderException(e.getMessage(), e.getCause());
		}
	}
	
}
