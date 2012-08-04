package org.sarons.spring4me.support.show.handler;

import java.text.DecimalFormat;

import org.sarons.spring4me.support.show.config.ViewItemConfig;
import org.springframework.util.Assert;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:32
 */
public class NumberFormatHandler extends AbstractHandler {

	private ThreadLocal<DecimalFormat> localDecimalFormat = new ThreadLocal<DecimalFormat>() {
		protected DecimalFormat initialValue() {
			return new DecimalFormat();
		};
	};
	
	@Override
	protected void handle(ViewItemConfig itemConfig, Object value) {
		if(value!=null && ViewItemConfig.Type.NUMBER==itemConfig.getType()) {
			Assert.hasText(itemConfig.getFormat());
			//
			DecimalFormat decimalFormat = localDecimalFormat.get();
			decimalFormat.applyPattern(itemConfig.getFormat());
			itemConfig.setValue(decimalFormat.format(value));
		}
	}
	
	
}
