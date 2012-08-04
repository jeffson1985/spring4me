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

package org.sarons.spring4me.web.page.tag.vm;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.sarons.spring4me.web.page.PlaceholderException;
import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.page.tag.PlaceholderCallback;
import org.sarons.spring4me.web.page.tag.PlaceholderProcessor;
import org.sarons.spring4me.web.widget.config.WidgetConfig;
import org.sarons.spring4me.web.widget.http.HttpWidget;
import org.sarons.spring4me.web.widget.utils.WidgetUtils;
import org.springframework.util.Assert;

/**
 * 
 * @author gavin
 * @since 0.4.0
 * @create Feb 12, 2012 - 11:31:57 AM
 */
public class PlaceholderDirective extends Directive {

	@Override
	public String getName() {
		return "placeholder";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(final InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		String groupId = null;
		//
		int num = node.jjtGetNumChildren();
		//
		if(num>0 && node.jjtGetChild(0)!=null) {
			groupId = String.valueOf(node.jjtGetChild(0).value(context));
		}
		//
		Assert.notNull(groupId, "Argument group id can not be null!");
		//
		PageConfig pageConfig = (PageConfig) context.get(PageConfig.KEY);
		//
		PlaceholderProcessor pp = new PlaceholderProcessor(pageConfig);
		pp.setCallback(new PlaceholderCallback(){

			public HttpWidget getHttpWidget(WidgetConfig widgetConfig) throws Exception {
				String httpWidgetKey = WidgetUtils.generateHttpWidgetKey(widgetConfig);
				return (HttpWidget) context.get(httpWidgetKey);
			}
			
		});
		//
		try {
			pp.process(writer, groupId);
			return true;
		} catch (Exception e) {
			throw new PlaceholderException(e.getMessage(), e.getCause());
		}
	}

}
