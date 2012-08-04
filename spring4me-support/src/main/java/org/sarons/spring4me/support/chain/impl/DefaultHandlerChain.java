package org.sarons.spring4me.support.chain.impl;

import java.util.ArrayList;
import java.util.List;

import org.sarons.spring4me.support.chain.Handler;
import org.sarons.spring4me.support.chain.HandlerChain;
import org.sarons.spring4me.support.chain.HandlerContext;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:51:09
 */
public class DefaultHandlerChain<T> implements HandlerChain<T> {
	
	private List<Handler<T>> handlers = new ArrayList<Handler<T>>();
	
	public void registerHandler(Handler<T> handler) {
		this.handlers.add(handler);
	}

	public T execute(HandlerContext context, T target) {
		for(Handler<T> handler : handlers) {
			if(!handler.handle(context, target)) {
				return target;
			}
		}
		return target;
	}

}
