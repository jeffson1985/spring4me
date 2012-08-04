package org.sarons.spring4me.support.show.handler;

import org.sarons.spring4me.support.chain.Handler;
import org.sarons.spring4me.support.chain.HandlerContext;
import org.sarons.spring4me.support.show.config.ViewConfig;
import org.sarons.spring4me.support.show.config.ViewItemConfig;
import org.springframework.beans.BeanWrapper;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:11
 */
public abstract class AbstractHandler implements Handler<BeanWrapper> {

	public boolean handle(HandlerContext context, BeanWrapper beanWrapper) {
		ViewConfig viewConfig = context.get(ViewConfig.class.getName());
		for(ViewItemConfig itemConfig : viewConfig.getItemConfigList()) {
			Object value = beanWrapper.getPropertyValue(itemConfig.getProperty());
			//
			handle(itemConfig, value);
		}
		return true;
	}

	protected abstract void handle(ViewItemConfig itemConfig, Object value);
	
}
