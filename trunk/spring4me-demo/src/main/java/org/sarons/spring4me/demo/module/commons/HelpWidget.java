package org.sarons.spring4me.demo.module.commons;

import org.sarons.spring4me.web.steroetype.Widget;
import org.springframework.web.bind.annotation.RequestMapping;

@Widget
@RequestMapping("/commons")
public class HelpWidget {

	@RequestMapping("/help")
	public String help() {
		return "commons/help";
	}
	
}
