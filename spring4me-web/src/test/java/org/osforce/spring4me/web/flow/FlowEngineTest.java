package org.osforce.spring4me.web.flow;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osforce.spring4me.web.flow.engine.FlowContext;
import org.osforce.spring4me.web.flow.engine.FlowEngine;
import org.osforce.spring4me.web.flow.engine.impl.DefaultFlowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="/applicationContext-flow.xml")
public class FlowEngineTest {

	@Autowired
	private FlowEngine flowEngine;
	
	@Test
	public void testExecute() {
		FlowContext context = new DefaultFlowContext("123456789");
		context.setFrom("").setTo("index");
		//
		String flowId = flowEngine.execute(context);
		//
		context = new DefaultFlowContext("123456789", flowId);
		context.setFrom("index").setTo("valid");
		//
		String flowId1 = flowEngine.execute(context);
		//
		Assert.assertEquals(flowId, flowId1);
		
		//
		context = new DefaultFlowContext("123456789", flowId1);
		context.setFrom("valid").setTo("start");
		String flowId2 = flowEngine.execute(context);
		//
		Assert.assertFalse(flowId1.equals(flowId2));
		//
		context = new DefaultFlowContext("123456789", flowId2);
		context.setFrom("finish").setTo("valid");
		String flowId3 = flowEngine.execute(context);
		System.out.println(flowId3);
		//
		context = new DefaultFlowContext("123456789", flowId3);
		context.setFrom("valid").setTo("end");
		String flowId4 = flowEngine.execute(context);
		Assert.assertEquals(flowId3, flowId4);
		//
		context = new DefaultFlowContext("123456789", flowId4);
		context.setFrom("end").setTo("other");
		String flowId5 = flowEngine.execute(context);
		Assert.assertNull(flowId5);
	}
	
	
	@Test
	public void testSubFlow() {
		FlowContext context = new DefaultFlowContext("123456789");
		context.setFrom("").setTo("index");
		//
		String flowId = flowEngine.execute(context);
		//
		context = new DefaultFlowContext("123456789", flowId);
		context.setFrom("index").setTo("valid");
		//
		String flowId1 = flowEngine.execute(context);
		//
		Assert.assertEquals(flowId, flowId1);
		
		//
		context = new DefaultFlowContext("123456789", flowId1);
		context.setFrom("valid").setTo("start");
		String flowId2 = flowEngine.execute(context);
		//
		Assert.assertFalse(flowId1.equals(flowId2));
		//
		context = new DefaultFlowContext("123456789", flowId2);
		context.setFrom("start").setTo("finish");
		String flowId3 = flowEngine.execute(context);
		System.out.println(flowId3);
		//
		context = new DefaultFlowContext("123456789", flowId3);
		context.setFrom("finish").setTo("start");
		String flowId4 = flowEngine.execute(context);
		System.out.println(flowId4);
		//
		context = new DefaultFlowContext("123456789", flowId4);
		context.setFrom("start").setTo("finish");
		String flowId5 = flowEngine.execute(context);
		System.out.println(flowId5);
		//
		context = new DefaultFlowContext("123456789", flowId5);
		context.setFrom("finish").setTo("valid");
		String flowId6 = flowEngine.execute(context);
		System.out.println(flowId6);
		//
		context = new DefaultFlowContext("123456789", flowId6);
		context.setFrom("valid").setTo("end");
		String flowId7 = flowEngine.execute(context);
		System.out.println(flowId7);
		//
		context = new DefaultFlowContext("123456789", flowId7);
		context.setFrom("end").setTo("other");
		String flowId8 = flowEngine.execute(context);
		System.out.println(flowId8);
	}
}
