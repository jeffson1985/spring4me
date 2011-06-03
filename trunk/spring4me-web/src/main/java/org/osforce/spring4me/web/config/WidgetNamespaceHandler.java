package org.osforce.spring4me.web.config;

import org.springframework.web.servlet.config.MvcNamespaceHandler;

public class WidgetNamespaceHandler extends MvcNamespaceHandler {

	public void init() {
		super.init();
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
	}

}
