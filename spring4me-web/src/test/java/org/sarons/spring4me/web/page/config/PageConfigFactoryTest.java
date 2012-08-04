package org.sarons.spring4me.web.page.config;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.sarons.spring4me.web.page.config.xml.XmlPageConfigFactory;
import org.springframework.core.io.DefaultResourceLoader;

public class PageConfigFactoryTest {

	private XmlPageConfigFactory pageConfigFactory;
	
	@Before
	public void setup() {
		pageConfigFactory = new XmlPageConfigFactory();
		pageConfigFactory.setPrefix("classpath:/pages/");
		pageConfigFactory.setResourceLoader(new DefaultResourceLoader());
	}
	
	@Test
	public void testFindPage() {
		PageConfig pageConfig = pageConfigFactory.findPage("index");
		Assert.assertEquals("index", pageConfig.getId());
	}
	
}
