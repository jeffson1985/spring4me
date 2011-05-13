package org.osforce.spring4me.demo.widget;

import org.osforce.spring4me.web.bind.annotation.Pref;
import org.osforce.spring4me.web.stereotype.Widget;
import org.springframework.web.bind.annotation.RequestMapping;

@Widget
@RequestMapping("test")
public class DemoWidget {

	public DemoWidget() {
	}

	@RequestMapping(value="/demo")
	public String test(@Pref Long numberValue,
			@Pref Boolean boolValue, @Pref String stringValue) {
		System.out.println(numberValue);
		System.out.println(boolValue);
		System.out.println(stringValue);
		return "test/demo";
	}

}
