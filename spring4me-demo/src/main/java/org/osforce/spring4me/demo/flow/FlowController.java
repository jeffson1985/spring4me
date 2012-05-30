package org.osforce.spring4me.demo.flow;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flow")
public class FlowController {

	@RequestMapping("/process")
	public void doProcessAction(@ModelAttribute FlowBean flowBean) {
		System.out.println(flowBean.getName());
		System.out.println(flowBean.getExtras());
	}
	
}
