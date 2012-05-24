package org.osforce.spring4me.support.service.impl;

import org.osforce.spring4me.support.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Gavin
 * @since 0.4.0
 * @create 2012-5-24 - 下午3:49:59
 */
@Service
public class DefaultHelloService implements HelloService {

	public String sayHello(String name) {
		return "hello, " + name;
	}

}
