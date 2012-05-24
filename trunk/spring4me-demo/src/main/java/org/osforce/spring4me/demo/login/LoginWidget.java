package org.osforce.spring4me.demo.login;

import org.osforce.spring4me.web.widget.steroetype.Widget;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 下午4:13:50
 */
@Widget
public class LoginWidget {
	
	@RequestMapping("/login")
	public String renderLoginView() {
		return "login/login-view";
	}
	
}
