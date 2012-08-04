package org.sarons.spring4me.support.chain;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:51:24
 * @param <T>
 */
public interface HandlerChain<T> {

	/**
	 * Register a handler to the chain
	 * @param handler
	 */
	void registerHandler(Handler<T> handler);
	
	/**
	 * Process a target using the chain
	 * @param context
	 * @param target
	 * @return
	 */
	T execute(HandlerContext context, T target);
	
}
