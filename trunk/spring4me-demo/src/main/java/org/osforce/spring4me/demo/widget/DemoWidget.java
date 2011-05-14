package org.osforce.spring4me.demo.widget;

import org.osforce.spring4me.web.bind.annotation.Pref;
import org.osforce.spring4me.web.stereotype.Widget;
import org.osforce.spring4me.web.widget.PageConfig;
import org.osforce.spring4me.web.widget.WidgetConfig;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Widget
@RequestMapping("test")
public class DemoWidget {

	public DemoWidget() {
	}

	@RequestMapping(value="/demo/{name}")
	public String test(@Pref Long numberValue, @Pref Boolean boolValue, 
			@Pref String stringValue, WidgetConfig widgetConfig, PageConfig pageConfig,
			@RequestParam Long userId, @PathVariable String name) {
		System.out.println(numberValue);
		System.out.println(boolValue);
		System.out.println(stringValue);
		System.out.println(widgetConfig);
		System.out.println(pageConfig);
		return "test/demo";
	}

}
