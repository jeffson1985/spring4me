package org.sarons.spring4me.demo.theme;

import org.sarons.spring4me.web.page.config.PageConfig;
import org.sarons.spring4me.web.page.utils.PageConfigUtils;
import org.sarons.spring4me.web.steroetype.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

@Page
public class ThemePage {

	@RequestMapping({"*.page", "/**/*.page"})
	public String apply(WebRequest webRequest) {
		PageConfig pageConfig = PageConfigUtils.getPageConfig(webRequest);
		return pageConfig.getLayout();
	}
}
