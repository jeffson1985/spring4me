package org.osforce.spring4me.web.flow.engine.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osforce.spring4me.web.flow.config.AbstractFlowContext;
import org.osforce.spring4me.web.flow.config.FlowConfig;
import org.osforce.spring4me.web.flow.config.StepConfig;
import org.osforce.spring4me.web.flow.engine.AbstractFlowEngine;
import org.osforce.spring4me.web.flow.engine.FlowEvent;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午8:49:59
 */
public class StandardFlowEngine extends AbstractFlowEngine {

	private static final Log log = LogFactory.getLog(StandardFlowEngine.class);
	
	@Override
	protected String processFlow(AbstractFlowContext flowContext, FlowConfig flowConfig) {
		StepConfig stepTo = flowConfig.getStepConfig(flowContext.getTo());
		StepConfig stepFrom = flowConfig.getStepConfig(flowContext.getFrom());
		if(stepTo!=null && stepFrom==null && StepConfig.Type.start==stepTo.getType()) {
			if(log.isDebugEnabled()) {
				log.debug(String.format("Starting the new flow %s", flowConfig.getName()));
			}
			//
			pushFlow(flowContext, flowConfig);
			//
			dispatchEvent(flowContext, flowConfig, FlowEvent.Type.FLOW_STARTING);
			//
			return flowConfig.getName();
		}
		//
		else if(stepFrom!=null && stepTo==null && StepConfig.Type.finish==stepFrom.getType()) {
			if(log.isDebugEnabled()) {
				log.debug(String.format("Finishing the flow %s", flowConfig.getName()));
			}
			//
			popFlow(flowContext);
			//
			if(!isFlowStackEmpty(flowContext)) {
				dispatchEvent(flowContext, flowConfig, FlowEvent.Type.SUBFLOW_FINISHING);
				//
				return peekFlow(flowContext);
			} else {
				dispatchEvent(flowContext, flowConfig, FlowEvent.Type.FLOW_FINISHING);
				//
				return null;
			}
		}
		//
		else if(stepFrom!=null && stepTo==null && stepFrom.hasSubFlow()) {
			FlowConfig subFlowConfig = getFlowConfigFactory().findSubFlow(
					flowConfig.getName(), flowContext.getFrom(), flowContext.getTo());
			if(log.isDebugEnabled()) {
				log.debug(String.format("Starting the sub flow %s", subFlowConfig.getName()));
			}
			//
			pushFlow(flowContext, subFlowConfig);
			//
			dispatchEvent(flowContext, subFlowConfig, FlowEvent.Type.SUBFLOW_STARTING);
			//
			return subFlowConfig.getName();
		} 
		else if(stepFrom!=null && stepTo!=null) {
			if(flowConfig.indexOf(stepFrom) < flowConfig.indexOf(stepTo)) {
				if(log.isDebugEnabled()) {
					log.debug("Flow forward ...");
				}
				//
				dispatchEvent(flowContext, flowConfig, FlowEvent.Type.FLOW_FORWARD);
			}
			if(flowConfig.indexOf(stepFrom) > flowConfig.indexOf(stepTo)) {
				if(log.isDebugEnabled()) {
					log.debug("Flow back ...");
				}
				//
				dispatchEvent(flowContext, flowConfig, FlowEvent.Type.FLOW_BACK);
			}
			//
			return flowConfig.getName();
		}
		//
		return flowConfig.getName();
	}
	
}
