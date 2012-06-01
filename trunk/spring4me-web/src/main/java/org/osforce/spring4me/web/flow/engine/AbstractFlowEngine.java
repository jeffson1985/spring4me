package org.osforce.spring4me.web.flow.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.osforce.spring4me.web.flow.config.AbstractFlowContext;
import org.osforce.spring4me.web.flow.config.FlowConfig;
import org.osforce.spring4me.web.flow.config.FlowConfigFactory;
import org.osforce.spring4me.web.flow.engine.FlowEvent.Type;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-31 - 上午9:15:31
 */
public abstract class AbstractFlowEngine implements FlowEngine, 
	BeanFactoryAware, InitializingBean {

	private BeanFactory beanFactory;
	
	private FlowConfigFactory flowConfigFactory;
	
	private List<FlowEventListener> eventListeners;
	
	private Map<String, Stack<String>> flowStackMap = new HashMap<String, Stack<String>>();
	
	public BeanFactory getBeanFactory() {
		return beanFactory;
	}
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public FlowConfigFactory getFlowConfigFactory() {
		return flowConfigFactory;
	}
	
	public void setEventListeners(List<FlowEventListener> eventListeners) {
		this.eventListeners = eventListeners;
	}
	
	public void afterPropertiesSet() throws Exception {
		this.flowConfigFactory = beanFactory.getBean(FlowConfigFactory.class);
	}
	
	public String execute(FlowContext context) {
		AbstractFlowContext flowContext = (AbstractFlowContext)context;
		//
		FlowConfig flowConfig = null;
		if(StringUtils.hasText(flowContext.getFlowId())) {
			flowConfig = getFlowConfigFactory().findFlow(flowContext.getFlowId());
		} else {
			flowConfig = getFlowConfigFactory().findFlow(flowContext.getFrom(), flowContext.getTo());
		}
		//
		return processFlow(flowContext, flowConfig);
	}
	
	protected void preProcessFlow(AbstractFlowContext flowContext, FlowConfig flowConfig) {}
	
	protected abstract String processFlow(AbstractFlowContext flowContext, FlowConfig flowConfig);
	
	protected void postProcessFlow(AbstractFlowContext flowContext, FlowConfig flowConfig) {
	}
	
	protected Stack<String> getFlowStack(AbstractFlowContext flowContext) {
		Stack<String> flowStack = this.flowStackMap.get(flowContext.getSessionKey());
		if(flowStack==null) {
			synchronized (this) {
				if(flowStack==null) {
					flowStack = new Stack<String>();
					this.flowStackMap.put(flowContext.getSessionKey(), flowStack);
				}
			}
		}
		//
		return flowStack;
	}
	
	protected boolean isFlowStackEmpty(AbstractFlowContext flowContext) {
		return getFlowStack(flowContext).isEmpty();
	}
	
	protected void pushFlow(AbstractFlowContext flowContext, FlowConfig flowConfig) {
		getFlowStack(flowContext).push(flowConfig.getName());
	}
	
	protected String popFlow(AbstractFlowContext flowContext) {
		return getFlowStack(flowContext).pop();
	}
	
	protected String peekFlow(AbstractFlowContext flowContext) {
		return getFlowStack(flowContext).peek();
	}
	
	protected void dispatchEvent(AbstractFlowContext flowContext, FlowConfig flowConfig, Type type) {
		FlowEvent flowEvent = new FlowEvent(flowContext, type);
		//
		for(FlowEventListener eventListener : eventListeners) {
			if(eventListener.accept(type)) {
				eventListener.trigger(flowEvent);
			}
		}
	}
}
