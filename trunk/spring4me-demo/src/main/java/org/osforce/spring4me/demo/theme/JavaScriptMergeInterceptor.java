package org.osforce.spring4me.demo.theme;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osforce.spring4me.web.page.config.PageConfig;
import org.osforce.spring4me.web.page.utils.PageConfigUtils;
import org.osforce.spring4me.web.widget.config.WidgetConfig;
import org.osforce.spring4me.web.widget.http.HttpWidgetRequest;
import org.osforce.spring4me.web.widget.utils.WidgetConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.util.WebUtils;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class JavaScriptMergeInterceptor extends HandlerInterceptorAdapter
	implements ResourceLoaderAware {

	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	private ResourceLoader resourceLoader;
	
	private String prefix = "/WEB-INF/widgets/";

	private String suffix = ".js";
	
	private ThreadLocal<List<String>> widgetViewNameLocal = new ThreadLocal<List<String>>();
	//
	private ThreadLocal<List<WidgetConfig>> widgetConfigLocal = new ThreadLocal<List<WidgetConfig>>();
	
	
	@Autowired
	public void setFreeMarkerConfigurer(
			FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}
	
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//
		String pathInfo = request.getPathInfo();
		if(request instanceof HttpWidgetRequest) {
			//
			WidgetConfig widgetConfig = WidgetConfigUtils.getWidgetConfig(request);
			//
			addWidgetConfigToThreadLocal(widgetConfig);
			//
			addWidgetViewNameToThreadLocal(modelAndView.getViewName());
			//
			pathInfo = (String) request.getAttribute(WebUtils.INCLUDE_PATH_INFO_ATTRIBUTE);
		}
		//
		if(pathInfo!=null && pathInfo.endsWith(".page")) {
			//
			generateAndMergeJavaScript(request);
			//
			clearWidgetViewNameLocal();
			//
			clearWidgetConfigLocal();
		}
	}
	
	private void addWidgetConfigToThreadLocal(WidgetConfig widgetConfig) {
		List<WidgetConfig> widgetConfigList = widgetConfigLocal.get();
		if(widgetConfigList==null) {
			widgetConfigList = new ArrayList<WidgetConfig>();
		}
		//
		widgetConfigList.add(widgetConfig);
		//
		widgetConfigLocal.set(widgetConfigList);
	}
	
	private void clearWidgetConfigLocal() {
		widgetConfigLocal.remove();
	}
	
	private void addWidgetViewNameToThreadLocal(String viewName) {
		List<String> widgetViewNames = widgetViewNameLocal.get();
		if(widgetViewNames==null) {
			widgetViewNames = new ArrayList<String>();
		}
		//
		widgetViewNames.add(viewName);
		//
		widgetViewNameLocal.set(widgetViewNames);
	}
	
	private void clearWidgetViewNameLocal() {
		widgetViewNameLocal.remove();
	}
	
	private void generateAndMergeJavaScript(HttpServletRequest request) 
			throws IOException, TemplateException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write("jQuery(function($){\n".getBytes());
		List<String> widgetViewNames = widgetViewNameLocal.get();
		List<WidgetConfig> widgetConfigList = widgetConfigLocal.get();
		for(int i=0; i<widgetViewNames.size(); i++) {
			String widgetViewName = widgetViewNames.get(i);
			WidgetConfig widgetConfig = widgetConfigList.get(i);
			String widgetJSLocation = prefix + widgetViewName + suffix;
			//
			Resource widgetJSResource = resourceLoader.getResource(widgetJSLocation);
			if(widgetJSResource.isReadable()) {
				baos.write(("// "+ widgetJSLocation + "\n").getBytes());
				//
				Map<String, Object> rootMap = new HashMap<String, Object>();
				rootMap.put("id", widgetConfig.getId());
				Template template = freeMarkerConfigurer.getConfiguration().getTemplate(widgetJSLocation);
				template.process(rootMap, new PrintWriter(baos));
				//
				baos.write("\n".getBytes());
			}
		}
		baos.write("});\n".getBytes());
		//
		copyGeneratedJavaScript(request, baos.toByteArray());
	}
	
	private void copyGeneratedJavaScript(HttpServletRequest request, byte[] javascript) throws IOException {
		PageConfig pageConfig = PageConfigUtils.getPageConfig(request);
		String jsFileName = pageConfig.getPath().substring(pageConfig.getPath().lastIndexOf("/")+1);
		String jsDirPath = pageConfig.getPath().substring(0, pageConfig.getPath().lastIndexOf("/"));
		//
		String jsBase = request.getSession().getServletContext().getRealPath("/WEB-INF/generate/js/");
		File jsDir = new File(jsBase + jsDirPath);
		jsDir.mkdirs();
		//
		File jsFile = new File(jsDir, jsFileName + ".js");
		if(!jsFile.exists()) {
			jsFile.createNewFile();
		}
		//
		FileCopyUtils.copy(javascript, jsFile);
	}
	
}
