package org.osforce.spring4me.web.view;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 *
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 14, 2011 - 6:03:59 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class ExtendsInternalResourceViewResolver extends
		InternalResourceViewResolver {
	
	private String themePrefix = "/WEB-INF/themes/";
	private String themeSuffix = ".jsp";
	
	public ExtendsInternalResourceViewResolver() {
		setPrefix("/WEB-INF/views/");
		setSuffix(".jsp");
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
		return ExtendsInternalResourceView.class;
	}
	
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		AbstractUrlBasedView view = super.buildView(viewName);
		if(view instanceof ExtendsInternalResourceView) {
			ExtendsInternalResourceView extendsView = (ExtendsInternalResourceView) view;
			extendsView.setThemePrefix(themePrefix);
			extendsView.setThemeSuffix(themeSuffix);
			return extendsView;
		}
		return view;
	}

}
