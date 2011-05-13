package org.osforce.spring4me.web.config;

import org.osforce.spring4me.web.config.AnnotationDrivenBeanDefinitionParser;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1
 * @create May 13, 2011 - 4:37:21 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class MvcNamespaceHandler extends
	org.springframework.web.servlet.config.MvcNamespaceHandler {

	@Override
	public void init() {
		super.init();
		// override spring's default annotation driven bean definition parser
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
	}

}
