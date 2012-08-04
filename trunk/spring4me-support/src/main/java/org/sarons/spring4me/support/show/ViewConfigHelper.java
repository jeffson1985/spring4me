package org.sarons.spring4me.support.show;

import org.sarons.spring4me.support.chain.HandlerChain;
import org.sarons.spring4me.support.chain.HandlerContext;
import org.sarons.spring4me.support.chain.impl.DefaultHandlerChain;
import org.sarons.spring4me.support.show.config.ViewConfig;
import org.sarons.spring4me.support.show.handler.CatchReplaceHandler;
import org.sarons.spring4me.support.show.handler.DateFormatHandler;
import org.sarons.spring4me.support.show.handler.DefaultValueHandler;
import org.sarons.spring4me.support.show.handler.NumberFormatHandler;
import org.sarons.spring4me.support.show.handler.StringFormatHandler;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:52:42
 */
public abstract class ViewConfigHelper {

	private static final HandlerChain<BeanWrapper> handlerChain;
	
	static {
		handlerChain = new DefaultHandlerChain<BeanWrapper>();
		handlerChain.registerHandler(new CatchReplaceHandler());
		handlerChain.registerHandler(new StringFormatHandler());
		handlerChain.registerHandler(new NumberFormatHandler());
		handlerChain.registerHandler(new DateFormatHandler());
		
		// 默认值处理 需要放在处理链的最后
		handlerChain.registerHandler(new DefaultValueHandler());
	}
	
	/**
	 * 对给定的对象应用显示配置
	 * @param viewConfig
	 * @param target
	 * @return 
	 */
	public static ViewConfig apply(ViewConfig viewConfig, Object target) {
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(target);
		HandlerContext context = new HandlerContext();
		context.set(ViewConfig.class.getName(), viewConfig);
		//
		handlerChain.execute(context, beanWrapper);
		//
		return context.get(ViewConfig.class.getName());
	}
	
}
