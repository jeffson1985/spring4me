package org.osforce.spring4me.demo.route;

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
public class RouteController {

	@RequestMapping(value={"/", "/home"}, method=RequestMethod.GET)
	public String route0() {
		return "page:/home";
	}

}
