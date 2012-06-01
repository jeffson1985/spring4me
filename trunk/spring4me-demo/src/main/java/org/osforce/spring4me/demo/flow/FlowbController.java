package org.osforce.spring4me.demo.flow;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/flowb")
public class FlowbController {

	@RequestMapping("/process")
	public void doProcessAction(@ModelAttribute FlowbBean flowbBean) {
		System.out.println(flowbBean.getName());
		System.out.println(flowbBean.getExtras());
	}
	
}
