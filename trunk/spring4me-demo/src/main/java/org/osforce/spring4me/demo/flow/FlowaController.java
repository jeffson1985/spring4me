package org.osforce.spring4me.demo.flow;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flowa")
public class FlowaController {

	@RequestMapping("/process")
	public void doProcessAction(@ModelAttribute FlowaBean flowaBean) {
		System.out.println(flowaBean.getName());
		System.out.println(flowaBean.getExtras());
	}
	
}
