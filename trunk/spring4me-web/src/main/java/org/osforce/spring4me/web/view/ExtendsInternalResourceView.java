package org.osforce.spring4me.web.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.widget.ConfigFactory;
import org.osforce.spring4me.web.widget.PageConfig;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1
 * @create May 13, 2011 - 4:37:51 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class ExtendsInternalResourceView extends InternalResourceView {

	private String themePrefix = "/WEB-INF/themes/";
	private String themeSuffix = ".jsp";

	public ExtendsInternalResourceView() {
	}

	public void setThemePrefix(String themePrefix) {
		this.themePrefix = themePrefix;
	}

	public void setThemeSuffix(String themeSuffix) {
		this.themeSuffix = themeSuffix;
	}

	@Override
	protected String prepareForRendering(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		if(StringUtils.startsWith(getBeanName(), "page:")) {
			String viewName = StringUtils.substringAfter(getBeanName(), "page:");
			ConfigFactory configFactory = getConfigFactory();
			//
			String theme = (String) request.getAttribute("theme");
			Locale locale = RequestContextUtils.getLocale(request);
			PageConfig pageConfig = configFactory.getPageConfig(viewName, locale);
			request.setAttribute(PageConfig.KEY, pageConfig);
			//
			return getLayout(theme);
		}
		return super.prepareForRendering(request, response);
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
				configFactory = getWebApplicationContext()
						.getBean("configFactory", ConfigFactory.class);
			} catch(NoSuchBeanDefinitionException e) {
					// if configFactory is null, get default config
				configFactory = getWebApplicationContext()
						.getBean(ConfigFactory.KEY, ConfigFactory.class);
			}
		}
		return configFactory;
	}

}
