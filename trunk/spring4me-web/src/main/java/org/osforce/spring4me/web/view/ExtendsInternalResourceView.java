package org.osforce.spring4me.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		ExtendsViewUtil viewUtil = new ExtendsViewUtil(themePrefix, themeSuffix);
		viewUtil.setWebApplicationContext(getWebApplicationContext());
		String url = viewUtil.getUrl(getBeanName(), request);
		if(url!=null) {
			return url;
		}
		return super.prepareForRendering(request, response);
	}

}
