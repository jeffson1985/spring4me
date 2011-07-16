package org.osforce.spring4me.web.tag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sf.ehcache.constructs.web.GenericResponseWrapper;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.cache.WidgetCacheService;
import org.osforce.spring4me.web.widget.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:37:42 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class PlaceholderTag extends TagSupport {
	private static final long serialVersionUID = 2860819904595893812L;

	private String name;
	private String openWith = "";
	private String closeWith = "";
	
	private WidgetCacheService  cacheService = null;

	public PlaceholderTag() {
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setOpenWith(String openWith) {
		this.openWith = openWith;
	}
	
	public void setCloseWith(String closeWith) {
		this.closeWith = closeWith;
	}

	@Override
	public int doEndTag() throws JspException {
		PageConfig pageConfig = (PageConfig) pageContext
				.getRequest().getAttribute(PageConfig.KEY);
		List<WidgetConfig> widgetConfigList = pageConfig.getWidgetConfigs(name);
		if(widgetConfigList==null || widgetConfigList.isEmpty()) {
			return EVAL_PAGE;
		}
		//
		StringBuffer buffer = new StringBuffer();
		for(WidgetConfig widgetConfig : widgetConfigList) {
			WidgetCacheService cacheService = getWidgetCacheService();
			Object value = cacheService.get(widgetConfig);
			//
			if(StringUtils.isBlank(widgetConfig.getCache()) || value==null) {
				pageContext.getRequest().setAttribute(WidgetConfig.KEY, widgetConfig);
				pageContext.getRequest().setAttribute(WidgetConfig.NAME, widgetConfig);
				try {
					final ByteArrayOutputStream baos = new ByteArrayOutputStream();
					final HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
					final HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
					pageContext.getRequest().getRequestDispatcher(widgetConfig.getPath())
							.include(request, new GenericResponseWrapper(response, baos));
					//
					value = (baos.toString("UTF-8"));
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				pageContext.getRequest().removeAttribute(WidgetConfig.NAME);
				pageContext.getRequest().removeAttribute(WidgetConfig.KEY);
			} 
			//
			if(StringUtils.isNotBlank(widgetConfig.getCache())) {
				cacheService.put(widgetConfig, value);
			}
			//
			if(StringUtils.isNotBlank((String) value))  {
				buffer.append((String) value);
			}
		}
		// 
		if(StringUtils.isNotBlank(buffer.toString())) {
			try {
				pageContext.getOut().write(openWith);
				pageContext.getOut().write(buffer.toString());
				pageContext.getOut().write(closeWith);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		return EVAL_PAGE;
	}
	
	protected PageConfig getPageConfig() {
		return (PageConfig) pageContext.getRequest().getAttribute(PageConfig.KEY);
	}
	
	protected WidgetCacheService getWidgetCacheService() {
		if(cacheService==null) {
			try {
				this.cacheService =  getWebApplicationContext().getBean(WidgetCacheService.class);
			} catch(NoSuchBeanDefinitionException e) {
				LoggerFactory.getLogger(WidgetCacheService.class).warn("No widget cache service configed!");
				//
				this.cacheService = new WidgetCacheService(){
					public Object get(Object key) {
						return null;
					}
					public void put(Object key, Object value) {
					}
				};
			}
		}
		return cacheService;
	}

	protected WebApplicationContext getWebApplicationContext() {
		return RequestContextUtils.getWebApplicationContext(pageContext.getRequest());
	}
	
}
