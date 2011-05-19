package org.osforce.spring4me.web.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.InternalResourceView;

/**
 *
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:37:51 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class ExtendsInternalResourceView extends InternalResourceView {

	private String themePrefix;
	private String themeSuffix;

	public ExtendsInternalResourceView() {
	}
	
	public void setThemePrefix(String prefix) {
		this.themePrefix = prefix;
	}
	
	public void setThemeSuffix(String suffix) {
		this.themeSuffix = suffix;
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
