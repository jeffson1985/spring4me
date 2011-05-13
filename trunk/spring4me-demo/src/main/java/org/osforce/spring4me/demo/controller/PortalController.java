package org.osforce.spring4me.demo.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
		return StringUtils.substringAfter(requestUri, contextPath);
	}

}
