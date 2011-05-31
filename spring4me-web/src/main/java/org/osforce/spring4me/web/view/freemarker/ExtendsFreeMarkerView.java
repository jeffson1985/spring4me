package org.osforce.spring4me.web.view.freemarker;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.web.view.ExtendsViewUtil;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

/**
 *
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 8:13:25 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class ExtendsFreeMarkerView extends FreeMarkerView {

	private static final String PREFIX_PAGE = "page:";

	private String themePrefix;
	private String themeSuffix;

	public ExtendsFreeMarkerView() {
	}
	
	public void setThemePrefix(String prefix) {
		this.themePrefix = prefix;
	}
	
	public void setThemeSuffix(String suffix) {
		this.themeSuffix = suffix;
	}

	@Override
	public boolean checkResource(Locale locale) throws Exception {
		if(StringUtils.startsWith(getBeanName(), PREFIX_PAGE)) {
			return true;
		}
		return super.checkResource(locale);
	}

	@Override
	protected void renderMergedTemplateModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ExtendsViewUtil viewUtil = new ExtendsViewUtil(themePrefix, themeSuffix);
		viewUtil.setWebApplicationContext(getWebApplicationContext());
		String url = viewUtil.getUrl(getBeanName(), request);
		//
		if(url!=null) {
			setUrl(url);
		}
		super.renderMergedTemplateModel(model, request, response);
	}

}
