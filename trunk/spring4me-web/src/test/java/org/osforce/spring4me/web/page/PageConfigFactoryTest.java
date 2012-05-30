package org.osforce.spring4me.web.page;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.config.PageConfigFactory;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-page.xml")
public class PageConfigFactoryTest {

	@Autowired
	private PageConfigFactory pageConfigFactory;
	
	@Test
	public void testFindPage() {
		PageConfig pageConfig = pageConfigFactory.findPage("/index");
		//
		for(WidgetConfig widgetConfig : pageConfig.getAllWidgetConfig()) {
			System.out.println(widgetConfig.getPath());
		}
	}
	
}
