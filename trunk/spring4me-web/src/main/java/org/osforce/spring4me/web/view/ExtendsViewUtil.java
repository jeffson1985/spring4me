package org.osforce.spring4me.web.view;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.widget.config.ConfigFactory;
import org.osforce.spring4me.web.widget.config.PageConfig;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:55:20 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
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
			String viewName = getViewName(beanName, request);
			ConfigFactory configFactory = getConfigFactory();
			//
			String theme = (String) request.getAttribute("theme");
			Locale locale = RequestContextUtils.getLocale(request);
			PageConfig pageConfig = configFactory.getPageConfig(viewName, locale);
			request.setAttribute(PageConfig.KEY, pageConfig);
			//
			Boolean popup = request.getAttribute("popup")!=null;
			return getLayout(theme, popup);
		}
		return null;
	}
	
	protected String getViewName(String beanName, HttpServletRequest request) {
		String viewName = StringUtils.substringAfter(beanName, PREFIX_PAGE);
		String domain = (String) request.getAttribute("domain");
		String qualifier = (String) request.getAttribute("qualifier");
		if(StringUtils.contains(viewName, "?")) {
			viewName += "&domain=" + domain; 
		} else {
			viewName += "?domain=" + domain;
		}
		//
		if(StringUtils.isNotBlank(qualifier)) {
			viewName +=  "&qualifier=" + qualifier;
		}
		return viewName;
	}

	protected String getLayout(String theme, Boolean popup) {
		String layout = popup ? "layout-popup" : "layout"; 
		theme = StringUtils.isBlank(theme) ? "default" : theme;
		return themePrefix + theme + "/" + layout + themeSuffix;
	}

	private ConfigFactory configFactory;
	protected ConfigFactory getConfigFactory() {
		if(configFactory == null) {
			configFactory = webApplicationContext.getBean(ConfigFactory.class);
		}
		return configFactory;
	}

}
