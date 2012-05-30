package org.osforce.spring4me.demo.flow;

import org.osforce.spring4me.web.widget.steroetype.Widget;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Widget
@RequestMapping("/flow")
public class FlowWidget {

	@RequestMapping("/start")
	public String renderFlowStartView(@ModelAttribute FlowBean flowBean) {
		flowBean.setName("Flow A");
		return "flow/flow-start";
	}
	
	@RequestMapping("/step")
	public String renderFlowStepView() {
		return "flow/flow-step";
	}
	
	@RequestMapping("/finish")
	public String renderFlowFinishView() {
		return "flow/flow-finish";
	}
	
}
