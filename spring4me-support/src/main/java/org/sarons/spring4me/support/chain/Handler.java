package org.sarons.spring4me.support.chain;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:51:14
 * @param <T>
 */
public interface Handler<T> {

	boolean handle(HandlerContext context, T target);
	
}
