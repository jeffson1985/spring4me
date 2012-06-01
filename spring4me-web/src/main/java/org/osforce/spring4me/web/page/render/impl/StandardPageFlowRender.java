package org.osforce.spring4me.web.page.render.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osforce.spring4me.web.Keys;
import org.osforce.spring4me.web.cache.CacheProvider;
import org.osforce.spring4me.web.cache.simple.RequestBackup;
import org.osforce.spring4me.web.flow.FlowNotFoundException;
import org.osforce.spring4me.web.flow.engine.FlowContext;
import org.osforce.spring4me.web.flow.engine.FlowEngine;
import org.osforce.spring4me.web.flow.engine.FlowEvent;
import org.osforce.spring4me.web.flow.engine.impl.WebFlowContext;
import org.osforce.spring4me.web.navigation.config.NavigationConfig;
import org.osforce.spring4me.web.navigation.utils.NavigationConfigUtils;
import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequestRestorable;
import org.osforce.spring4me.web.widget.http.HttpWidgetResponse;
import org.osforce.spring4me.web.widget.http.impl.DefaultHttpWidgetRequestRestorable;
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
	
	private FlowEngine flowEngine;
	
	private CacheProvider requestBackupCacheProvider;
	
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
	
	public void afterPropertiesSet() throws Exception {
		this.flowEngine = beanFactory.getBean(FlowEngine.class);
		this.requestBackupCacheProvider = beanFactory.getBean("requestBackupCacheProvider", CacheProvider.class);
	}
	
	@Override
	protected void prePageRender(PageConfig pageConfig,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		//
		try {
			NavigationConfig toNavigationConfig = NavigationConfigUtils.getToNavigationConfig(httpRequest);
			NavigationConfig fromNavigationConfig = NavigationConfigUtils.getFromNavigationConfig(httpRequest);
			//
			String toPageId = (toNavigationConfig!=null ? toNavigationConfig.getId() : null);
			String fromPageId = (fromNavigationConfig!=null ? fromNavigationConfig.getId() : null);
			
			FlowContext context = new WebFlowContext(httpRequest, httpResponse);
			context.setFrom(fromPageId).setTo(toPageId);
			//
			String flowId = flowEngine.execute(context);
			httpRequest.setAttribute("flowId", flowId);
			//
		} catch(FlowNotFoundException e) {
			log.debug(e);
		} finally {
			super.prePageRender(pageConfig, httpRequest, httpResponse);
		}
	}
	
	@Override
	protected HttpWidgetRequest createHttpWidgetRequest(HttpServletRequest httpRequest) {
		HttpWidgetRequest widgetRequest = super.createHttpWidgetRequest(httpRequest);
		return new DefaultHttpWidgetRequestRestorable(widgetRequest);
	}
	
	@Override
	protected void preWidgetRender(WidgetConfig widgetConfig,
			HttpWidgetRequest widgetRequest, HttpWidgetResponse widgetResponse) {
		super.preWidgetRender(widgetConfig, widgetRequest, widgetResponse);
		//
		HttpWidgetRequestRestorable restorableWidgetRequest = (HttpWidgetRequestRestorable) widgetRequest;
		//
		FlowEvent flowEvent = (FlowEvent) widgetRequest.getAttribute(Keys.REQUEST_KEY_FLOW_EVENT);
		if(flowEvent!=null && (FlowEvent.Type.FLOW_BACK==flowEvent.getType() 
				|| FlowEvent.Type.SUBFLOW_FINISHING==flowEvent.getType())) {
			//
			String key = generateKey(widgetRequest, widgetConfig);
			RequestBackup requestBackup = (RequestBackup) this.requestBackupCacheProvider.getCache().get(key);
			if(requestBackup!=null) {
				Map<String, String[]> parameterMap = requestBackup.getParameterBackup();
				Map<String, Object> attributesMap = requestBackup.getAttributeBackup();
				//
				restorableWidgetRequest.restoreWidgetRequest(parameterMap, attributesMap);
			}
		}
	}
	
	@Override
	protected void postWidgetRender(WidgetConfig widgetConfig,
			HttpWidgetRequest widgetRequest, HttpWidgetResponse widgetResponse) {
		//
		FlowEvent flowEvent = (FlowEvent) widgetRequest.getAttribute(Keys.REQUEST_KEY_FLOW_EVENT);
		if(flowEvent!=null) {
			// backup
			String key = generateKey(widgetRequest, widgetConfig);
			RequestBackup requestBackup = (RequestBackup) requestBackupCacheProvider.getCache().get(key);
			if(requestBackup==null) {
				requestBackup = new RequestBackup(widgetRequest);
			} else {
				requestBackup.mergeRequest(widgetRequest);
			}
			this.requestBackupCacheProvider.getCache().put(key, requestBackup);
		}
		//
		super.postWidgetRender(widgetConfig, widgetRequest, widgetResponse);
	}
	
	private String generateKey(HttpWidgetRequest widgetRequest, WidgetConfig widgetConfig) {
		return widgetRequest.getSession().getId() + "T" + widgetConfig.getPath();
	}
	
}
