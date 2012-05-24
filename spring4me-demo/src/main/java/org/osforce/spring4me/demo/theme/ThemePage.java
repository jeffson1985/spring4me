package org.osforce.spring4me.demo.theme;

import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.steroetype.Page;
import org.osforce.spring4me.web.page.utils.PageConfigUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Page
public class ThemePage {

	@RequestMapping
	public String beautifyPage(WebRequest webRequest) {
		PageConfig pageConfig = PageConfigUtils.getPageConfig(webRequest);
		return "default/"+pageConfig.getTemplate();
	}
	
}
