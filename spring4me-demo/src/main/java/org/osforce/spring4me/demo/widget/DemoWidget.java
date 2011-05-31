package org.osforce.spring4me.demo.widget;

import org.osforce.spring4me.web.bind.annotation.PrefParam;
import org.osforce.spring4me.web.stereotype.Widget;
import org.osforce.spring4me.web.widget.PageConfig;
import org.osforce.spring4me.web.widget.WidgetConfig;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 16, 2011 - 3:52:47 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
@Widget
@RequestMapping("test")
public class DemoWidget {

	public DemoWidget() {
	}

	@RequestMapping(value="/demo")
	public String test(@PrefParam Long numberValue, @PrefParam Boolean boolValue, 
			@PrefParam String stringValue, WidgetConfig widgetConfig, PageConfig pageConfig) {
		System.out.println(numberValue);
		System.out.println(boolValue);
		System.out.println(stringValue);
		System.out.println(widgetConfig);
		System.out.println(pageConfig);
		return "test/demo";
	}

}
