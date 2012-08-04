package org.sarons.spring4me.support.show.handler;

import org.sarons.spring4me.support.show.config.ViewItemConfig;
import org.springframework.util.StringUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:36
 */
public class StringFormatHandler extends AbstractHandler {
	
	@Override
	protected void handle(ViewItemConfig itemConfig, Object value) {
		if(value!=null && ViewItemConfig.Type.STRING==itemConfig.getType()
				&& StringUtils.hasText(itemConfig.getFormat())) {
			//
			itemConfig.setValue(String.format(itemConfig.getFormat(), value));
		}
	}

}
