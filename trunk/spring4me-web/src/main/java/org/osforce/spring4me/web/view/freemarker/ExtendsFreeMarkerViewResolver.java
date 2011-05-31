package org.osforce.spring4me.web.view.freemarker;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 14, 2011 - 6:04:59 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class ExtendsFreeMarkerViewResolver extends FreeMarkerViewResolver {

	private String themePrefix = "/WEB-INF/themes/";
	private String themeSuffix = ".ftl";
	
	public ExtendsFreeMarkerViewResolver() {
		setPrefix("/WEB-INF/views/");
		setSuffix(".ftl");
	}
	
	public void setThemePrefix(String themePrefix) {
		this.themePrefix = themePrefix;
	}
	
	public void setThemeSuffix(String themeSuffix) {
		this.themeSuffix = themeSuffix;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected Class requiredViewClass() {
		return ExtendsFreeMarkerView.class;
	}

	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		if(view instanceof ExtendsFreeMarkerView) {
			ExtendsFreeMarkerView extendsView = (ExtendsFreeMarkerView) view;
			extendsView.setThemePrefix(themePrefix);
			extendsView.setThemeSuffix(themeSuffix);
			return extendsView;
		}
		return view;
	}
	
}
