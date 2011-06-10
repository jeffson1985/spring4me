package org.osforce.spring4me.demo.widget;

import org.osforce.spring4me.web.bind.annotation.PrefParam;
import org.osforce.spring4me.web.stereotype.Widget;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:52:47 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
@Widget
@RequestMapping("/commons")
public class ContentWidget {

	public ContentWidget() {
	}

	@RequestMapping(value="/content")
	public String test(@PrefParam String content, Model model) {
		model.addAttribute("content", content);
		return "commons/content";
	}

}
