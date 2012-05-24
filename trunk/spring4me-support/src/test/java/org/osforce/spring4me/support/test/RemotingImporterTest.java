package org.osforce.spring4me.support.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.support.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-importer.xml")
public class RemotingImporterTest {

	@Autowired
	private HelloService helloService;
	
	@Test
	public void testHello() {
		String msg = helloService.sayHello("gavin");
		System.out.println(msg);
	}
	
}
