package org.osforce.spring4me.demo.login;

import javax.validation.Valid;

import org.osforce.spring4me.web.event.EventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public void doLoginAction(@ModelAttribute @Valid User user, WebRequest webRequest) {
		//
		System.out.println("do login...");
		//
		if("haozhonghu@hotmail.com".equals(user.getUsername()) && "123456".equals(user.getPassword())) {
			EventPublisher.publish(webRequest, "login-success");
		} else {
			EventPublisher.publish(webRequest, "login-error");
		}
	}
	
}
