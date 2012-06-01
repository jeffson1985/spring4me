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

package org.osforce.spring4me.web.navigation.tag.vm;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.navigation.tag.NavigationProcessor;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.springframework.util.Assert;

public class NavigationDirective extends Directive {

	@Override
	public String getName() {
		return "navigation";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer,
			Node node) throws IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		String event = null;
		String action = null;
		//
		int num = node.jjtGetNumChildren();
		//
		if(num>0 && node.jjtGetChild(0)!=null) {
			event = String.valueOf(node.jjtGetChild(0).value(context));
		}
		if(num>1 && node.jjtGetChild(1)!=null) {
			String base = (String) context.get(Keys.REQUEST_KEY_BASE);
			action = base + String.valueOf(node.jjtGetChild(1).value(context));
		}
		//
		Assert.notNull(event, "Argument event can not be null!");
		//
		WidgetConfig widgetConfig = (WidgetConfig) context.get(Keys.REQUEST_KEY_WIDGET_CONFIG);
		String eventDrivenServiceUrl = (String) context.get(Keys.REQUEST_KEY_EVENT_DRIVEN_SERVICE_URL);
		//
		NavigationProcessor processor = new NavigationProcessor(widgetConfig, eventDrivenServiceUrl);
		processor.process(writer, event, action);
		return true;
	}

}