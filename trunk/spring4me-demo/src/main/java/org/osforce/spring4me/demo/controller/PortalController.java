package org.osforce.spring4me.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:52:40 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
@Controller
public class PortalController {

	@RequestMapping(value="/**", method=RequestMethod.GET)
	public String render(HttpServletRequest request) {
		String requestPath = getRequestPage(request);
		return "page:" + requestPath;
	}

	private String getRequestPage(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String requestPath = StringUtils.substringAfter(requestUri, contextPath);
		if(StringUtils.isBlank("") || StringUtils.equals(requestPath, "/")) {
			requestPath = "/home";
		}
		return requestPath;
	}

}
