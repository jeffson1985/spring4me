package org.osforce.spring4me.web.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.osforce.spring4me.web.widget.PageConfig;
import org.osforce.spring4me.web.widget.WidgetConfig;

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

	public PlaceholderTag() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int doEndTag() throws JspException {
		PageConfig pageConfig = (PageConfig) pageContext
				.getRequest().getAttribute(PageConfig.KEY);
		List<WidgetConfig> widgetConfigList = pageConfig.getWidgetConfigs(name);
		for(WidgetConfig widgetConfig : widgetConfigList) {
			pageContext.getRequest().setAttribute(WidgetConfig.KEY, widgetConfig);
			try {
				pageContext.include(widgetConfig.getPath());
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return EVAL_PAGE;
	}

}
