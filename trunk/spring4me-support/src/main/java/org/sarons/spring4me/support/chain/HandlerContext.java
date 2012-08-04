package org.sarons.spring4me.support.chain;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:51:30
 */
public final class HandlerContext {

	private Map<String, Object> model = new HashMap<String, Object>();
	
	public void set(String name, Object value) {
		this.model.put(name, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String name) {
		return (T) model.get(name);
	}
	
}
