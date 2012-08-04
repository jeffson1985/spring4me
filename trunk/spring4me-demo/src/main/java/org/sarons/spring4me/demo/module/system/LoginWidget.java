package org.sarons.spring4me.demo.module.system;

import org.sarons.spring4me.web.steroetype.Widget;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Widget
@RequestMapping("/system")
public class LoginWidget {
	
	@RequestMapping("/login-form")
	public String login_form(@ModelAttribute User user) {
		return "system/login-form";
	}
	
	@RequestMapping("/login")
	public String login(@ModelAttribute @Validated User user) {
		//
		if("gavin".equals(user.getUsername()) && "123123".equals(user.getPassword())) {
			return "event:login-success";
		}
		//
		return "event:login-fail";
	}
	
}
