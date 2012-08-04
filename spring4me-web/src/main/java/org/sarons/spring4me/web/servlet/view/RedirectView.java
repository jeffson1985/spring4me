package org.sarons.spring4me.web.servlet.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:50:53
 */
public class RedirectView extends
		org.springframework.web.servlet.view.RedirectView {
	
	public RedirectView() {
		super();
	}
	
	public RedirectView(String url) {
		super(url);
	}
	
	public RedirectView(String url, boolean contextRelative) {
		super(url, contextRelative);
	}
	
	public RedirectView(String url, boolean contextRelative, boolean http10Compatible) {
		super(url, contextRelative, http10Compatible);
	}
	
	public RedirectView(String url, boolean contextRelative, boolean http10Compatible, boolean exposeModelAttributes) {
		super(url, contextRelative, http10Compatible, exposeModelAttributes);
	}
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		//
		String targetUrl = createTargetUrl(model, request);

		targetUrl = updateTargetUrl(targetUrl, model, request, response);
		
		FlashMap flashMap = RequestContextUtils.getOutputFlashMap(request);
		flashMap.putAll(model);
		
		FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
		flashMapManager.saveOutputFlashMap(flashMap, request, response);

		sendRedirect(request, response, targetUrl.toString(), true);
	}
	
}
