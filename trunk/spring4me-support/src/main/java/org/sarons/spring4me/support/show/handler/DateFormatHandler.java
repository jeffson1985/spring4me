package org.sarons.spring4me.support.show.handler;

import java.text.SimpleDateFormat;

import org.sarons.spring4me.support.show.config.ViewItemConfig;
import org.springframework.util.Assert;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:21
 */
public class DateFormatHandler extends AbstractHandler {

	private ThreadLocal<SimpleDateFormat> dataFormatLocal = new ThreadLocal<SimpleDateFormat>(){
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat();
		};
	};
	
	@Override
	protected void handle(ViewItemConfig itemConfig, Object value) {
		if(value!=null && ViewItemConfig.Type.DATE==itemConfig.getType()) {
			Assert.hasText(itemConfig.getFormat());
			//
			SimpleDateFormat dateFormat = dataFormatLocal.get();
			dateFormat.applyPattern(itemConfig.getFormat());
			itemConfig.setValue(dateFormat.format(value));
		}
	}

}
