package org.osforce.spring4me.web.navigation;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.web.navigation.config.NavigationConfig;
import org.osforce.spring4me.web.navigation.config.NavigationConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-navigation.xml")
public class NavigationConfigFactoryTest  {

	@Autowired
	private NavigationConfigFactory navigationConfigFactory;
	
	@Test
	public void testFindNavigation() {
		NavigationConfig navigationConfig = navigationConfigFactory.findNavigation("/index");
		Assert.assertEquals("index", navigationConfig.getId());
	}
	
}
