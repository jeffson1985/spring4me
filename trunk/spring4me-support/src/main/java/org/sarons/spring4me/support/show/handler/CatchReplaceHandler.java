package org.sarons.spring4me.support.show.handler;

import org.sarons.spring4me.support.show.config.ViewItemConfig;
import org.springframework.util.StringUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:16
 */
public class CatchReplaceHandler extends AbstractHandler {

	@Override
	protected void handle(ViewItemConfig itemConfig, Object value) {
		if(value!=null && StringUtils.hasText(itemConfig.getCatchValue())) {
			if(itemConfig.getCatchValue().equals(String.valueOf(value))) {
				itemConfig.setValue(itemConfig.getReplaceValue());
			}
		}
	}

}
