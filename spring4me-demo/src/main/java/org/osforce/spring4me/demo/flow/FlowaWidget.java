package org.osforce.spring4me.demo.flow;

import org.osforce.spring4me.web.widget.steroetype.Widget;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Widget
@RequestMapping("/flowa")
public class FlowaWidget {

	@RequestMapping("/start")
	public String renderFlowStartView(@ModelAttribute FlowaBean flowaBean,
			@RequestParam(required=false, defaultValue="Flow A") String name, 
			@RequestParam(required=false) String extras) {
		flowaBean.setName(name);
		flowaBean.setExtras(extras);
		return "flow/flowa-start";
	}
	
	@RequestMapping("/step")
	public String renderFlowStepView() {
		return "flow/flowa-step";
	}
	
	@RequestMapping("/finish")
	public String renderFlowFinishView() {
		return "flow/flowa-finish";
	}
	
}
