package org.osforce.spring4me.demo.theme;

import javax.servlet.http.HttpServletRequest;

import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.steroetype.Page;
import org.osforce.spring4me.web.page.utils.PageConfigUtils;
import org.springframework.ui.context.Theme;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

@Page
public class ThemePage {

	@RequestMapping("*.page")
	public String beautifyPage(HttpServletRequest request) {
		Theme theme = RequestContextUtils.getTheme(request);
		PageConfig pageConfig = PageConfigUtils.getPageConfig(request);
		return theme.getName() + "/" + pageConfig.getTemplate();
	}
	
}
