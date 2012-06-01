package org.osforce.spring4me.demo.flow;

import org.osforce.spring4me.web.widget.steroetype.Widget;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Widget
@RequestMapping("/flowb")
public class FlowbWidget {

	@RequestMapping("/start")
	public String renderFlowStartView(@ModelAttribute FlowbBean flowbBean,
			@RequestParam(required=false, defaultValue="Flow B") String name, 
			@RequestParam(required=false) String extras) {
		flowbBean.setName(name);
		flowbBean.setExtras(extras);
		return "flow/flowb-start";
	}
	
	@RequestMapping("/step")
	public String renderFlowStepView() {
		return "flow/flowb-step";
	}
	
	@RequestMapping("/finish")
	public String renderFlowFinishView() {
		return "flow/flowb-finish";
	}
	
}
