package org.sarons.spring4me.support.show.handler;

import org.sarons.spring4me.support.show.config.ViewItemConfig;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:26
 */
public class DefaultValueHandler extends AbstractHandler {

	@Override
	protected void handle(ViewItemConfig itemConfig, Object value) {
		if(itemConfig.getValue()==null) {
			itemConfig.setValue(String.valueOf(value));
		}
	}

}
