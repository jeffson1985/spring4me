package org.sarons.spring4me.support.chain;

import org.junit.Test;
import org.sarons.spring4me.support.chain.Handler;
import org.sarons.spring4me.support.chain.HandlerChain;
import org.sarons.spring4me.support.chain.HandlerContext;
import org.sarons.spring4me.support.chain.impl.DefaultHandlerChain;


public class HandlerChainTest {

	@Test
	public void testChain() {
		//
		HandlerChain<HandleTarget> chain = new DefaultHandlerChain<HandleTarget>();
		chain.registerHandler(new Handler<HandleTarget>(){

			public boolean handle(HandlerContext context, HandleTarget target) {
				target.setMessage("hello chain!");
				return false;
			}
			
		});
		chain.registerHandler(new Handler<HandleTarget>(){

			public boolean handle(HandlerContext context, HandleTarget target) {
				target.setMessage("hello chain2!");
				return true;
			}
			
		});
		//
		HandleTarget target = (HandleTarget) chain.execute(new HandlerContext(), new HandleTarget());
		System.out.println(target.getMessage());
	}
	
}
