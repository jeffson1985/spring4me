package org.osforce.spring4me.demo.theme.support;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.ui.context.support.ResourceBundleThemeSource;

public class ReloadableResourceBundleThemeSource extends
		ResourceBundleThemeSource {

	@Override
	protected MessageSource createMessageSource(String basename) {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename(basename);
		return messageSource;
	}
	
}
