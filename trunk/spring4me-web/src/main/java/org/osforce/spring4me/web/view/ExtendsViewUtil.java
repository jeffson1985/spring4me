package org.osforce.spring4me.web.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.widget.ConfigFactory;
import org.osforce.spring4me.web.widget.PageConfig;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

public class ExtendsViewUtil {
	private static final String PREFIX_PAGE = "page:";

	private String themePrefix;
	private String themeSuffix;

	private WebApplicationContext webApplicationContext;

	public ExtendsViewUtil(String prefix, String suffix) {
		this.themePrefix = prefix;
		this.themeSuffix = suffix;
	}

	public void setWebApplicationContext(
			WebApplicationContext webApplicationContext) {
		this.webApplicationContext = webApplicationContext;
	}

	public String getUrl(String beanName, HttpServletRequest request) throws Exception {
		if(StringUtils.startsWith(beanName, PREFIX_PAGE)) {
			String viewName = StringUtils.substringAfter(beanName, PREFIX_PAGE);
			ConfigFactory configFactory = getConfigFactory();
			//
			String theme = (String) request.getAttribute("theme");
			Locale locale = RequestContextUtils.getLocale(request);
			PageConfig pageConfig = configFactory.getPageConfig(viewName, locale);
			request.setAttribute(PageConfig.KEY, pageConfig);
			//
			return getLayout(theme);
		}
		return null;
	}

	protected String getLayout(String theme) {
		String layout = themePrefix + "default/layout" + themeSuffix;
		if(StringUtils.isNotBlank(theme)) {
			layout = themePrefix + theme + "/layout" + themeSuffix;
		}
		return layout;
	}

	private ConfigFactory configFactory;
	protected ConfigFactory getConfigFactory() {
		if(configFactory == null) {
			try {
				configFactory = webApplicationContext
						.getBean("configFactory", ConfigFactory.class);
			} catch(NoSuchBeanDefinitionException e) {
					// if configFactory is null, get default config
				configFactory = webApplicationContext
						.getBean(ConfigFactory.KEY, ConfigFactory.class);
			}
		}
		return configFactory;
	}

}
