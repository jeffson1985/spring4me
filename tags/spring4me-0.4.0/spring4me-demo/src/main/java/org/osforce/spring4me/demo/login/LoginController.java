package org.osforce.spring4me.demo.login;

import org.osforce.spring4me.web.event.EventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 下午4:13:46
 */
@Controller
public class LoginController {

	@RequestMapping("/login.do")
	public void doLoginAction(WebRequest webRequest, 
			@RequestParam String username, @RequestParam String password) {
		//
		System.out.println("do login...");
		//
		if("gavin".equals(username) && "123456".equals(password)) {
			EventPublisher.publish(webRequest, "login-success");
		} else {
			EventPublisher.publish(webRequest, "login-error");
		}
	}
	
}
