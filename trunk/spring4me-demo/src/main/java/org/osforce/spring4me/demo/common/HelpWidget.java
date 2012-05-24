package org.osforce.spring4me.demo.common;

import org.osforce.spring4me.web.widget.steroetype.Widget;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-23 - 下午4:13:41
 */
@Widget
public class HelpWidget {

	@RequestMapping("/help")
	public String renderHelpView() {
		return "common/help-view";
	}
	
}
