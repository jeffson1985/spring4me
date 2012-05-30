package org.osforce.spring4me.web.page.render.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.flow.FlowNotFoundException;
import org.osforce.spring4me.web.flow.config.FlowConfig;
import org.osforce.spring4me.web.flow.config.FlowConfigFactory;
import org.osforce.spring4me.web.flow.config.StepConfig;
import org.osforce.spring4me.web.flow.utils.FlowConfigUtils;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.utils.PageConfigUtils;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.http.HttpWidgetResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午7:30:14
 */
public class StandardPageFlowRender extends StandardPageRender 
	implements BeanFactoryAware, InitializingBean {
	
	private static final Log log = LogFactory.getLog(StandardPageFlowRender.class);

	private BeanFactory beanFactory;
	private FlowConfigFactory flowConfigFactory;
	//
	private ThreadLocal<FlowConfig> flowConfigLocal = new ThreadLocal<FlowConfig>();
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public FlowConfigFactory getFlowConfigFactory() {
		return flowConfigFactory;
	}
	
	public void afterPropertiesSet() throws Exception {
		this.flowConfigFactory = beanFactory.getBean(FlowConfigFactory.class);
	}
	
	@Override
	protected void prePageRender(PageConfig pageConfig,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		//
		PageConfig current = pageConfig;
		PageConfig original = (PageConfig) httpRequest.getAttribute(Keys.REQUEST_KEY_ORIGINAL_PAGE_CONFIG);
		//
		try {
			String currentPageId = current.getId();
			String originalPageId = (original!=null ? original.getId() : null);
			FlowConfig flowConfig = flowConfigFactory.findFlow(originalPageId, currentPageId);
			//
			this.flowConfigLocal.set(flowConfig);
			//
		} catch(FlowNotFoundException e) {
			log.debug(e);
		} finally {
			super.prePageRender(pageConfig, httpRequest, httpResponse);
		}
	}
	
	@Override
	protected void preWidgetRender(WidgetConfig widgetConfig, 
			HttpWidgetRequest widgetRequest, HttpWidgetResponse widgetResponse) {
		//
		FlowConfig flowConfig = this.flowConfigLocal.get();
		PageConfig pageConfig = PageConfigUtils.getPageConfig(widgetRequest);
		//
		if(flowConfig!=null) {
			//
			processFlow(widgetRequest, flowConfig, pageConfig);
			//
			FlowConfigUtils.setFlowConfig(widgetRequest, flowConfig);
		}
		//
		super.preWidgetRender(widgetConfig, widgetRequest, widgetResponse);
	}
	
	
	protected void processFlow(HttpWidgetRequest widgetRequest, 
			FlowConfig flowConfig, PageConfig pageConfig) {
		//
		StepConfig stepConfig = flowConfig.getStepConfig(pageConfig.getId());
		//
		if(StepConfig.Type.start==stepConfig.getType()) {
			processFlowStart(widgetRequest, flowConfig, pageConfig, stepConfig);
		} 
		else if(StepConfig.Type.finish==stepConfig.getType()) {
			processFlowFinish(widgetRequest, flowConfig, pageConfig, stepConfig);
		} 
		else {
			processFlowStep(widgetRequest, flowConfig, pageConfig, stepConfig);
		}
		//
	}
	
	protected void processFlowStart(HttpWidgetRequest widgetRequest, 
			FlowConfig flowConfig, PageConfig pageConfig, StepConfig start) {
		
	}
	
	protected void processFlowStep(HttpWidgetRequest widgetRequest, 
			FlowConfig flowConfig, PageConfig pageConfig, StepConfig step) {
		
	}
	
	protected void processFlowFinish(HttpWidgetRequest widgetRequest, 
			FlowConfig flowConfig, PageConfig pageConfig, StepConfig finish) {
		
	}
	
	@Override
	protected void postWidgetRender(WidgetConfig widgetConfig, 
			HttpWidgetRequest widgetRequest, HttpWidgetResponse widgetResponse) {
		//
		this.flowConfigLocal.remove();
		//
		super.postWidgetRender(widgetConfig, widgetRequest, widgetResponse);
	}
	
}
