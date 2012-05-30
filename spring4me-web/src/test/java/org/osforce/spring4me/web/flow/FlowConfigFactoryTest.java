package org.osforce.spring4me.web.flow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.web.flow.config.FlowConfig;
import org.osforce.spring4me.web.flow.config.FlowConfigFactory;
import org.osforce.spring4me.web.flow.config.StepConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-flow.xml")
public class FlowConfigFactoryTest {

	@Autowired
	private FlowConfigFactory flowConfigFactory;
	
	@Test
	public void testFindFlow() {
		FlowConfig flowConfig = flowConfigFactory.findFlow(null, "index");
		//
		StepConfig current = flowConfig.getStepConfig("valid");
		StepConfig previous = flowConfig.getPreStepConfig("valid");
		StepConfig next = flowConfig.getNextStepConfig("valid");
		//
		System.out.println(previous.getPage());
		System.out.println(current.getPage());
		System.out.println(next.getPage());
	}
	
}
