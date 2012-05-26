package org.osforce.spring4me.web.navigation;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.web.navigation.config.SiteConfig;
import org.osforce.spring4me.web.navigation.config.SiteConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-navigation.xml")
public class SiteConfigFactoryTest  {

	@Autowired
	private SiteConfigFactory siteConfigFactory;
	
	@Test
	public void testFindSite() {
		SiteConfig siteConfig = siteConfigFactory.findSite();
		Assert.assertEquals(2, siteConfig.getAllNavigationConfig().size());
	}
	
}
