package org.osforce.spring4me.demo.theme;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidatorFactory;

import org.osforce.spring4me.demo.theme.support.AbstractClassFinder;
import org.osforce.spring4me.demo.theme.support.ValidationBeanClassFinder;
import org.osforce.spring4me.support.validation.BeanValidationHelper;
import org.osforce.spring4me.support.validation.ValidateBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class JavaScriptGenerateInterceptor extends HandlerInterceptorAdapter {
	
	private static final Pattern pattern = Pattern.compile("generate/js/validate/(\\w*?)\\.validate\\.js");
	
	private String basePackage = "/**/";
	
	private ValidatorFactory validatorFactory;
	
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	private AbstractClassFinder validationBeanClassFinder = new ValidationBeanClassFinder();
	
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	
	@Autowired
	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		this.validatorFactory = validatorFactory;
	}
	
	@Autowired
	public void setFreeMarkerConfigurer(
			FreeMarkerConfigurer freeMarkerConfigurer) {
		this.freeMarkerConfigurer = freeMarkerConfigurer;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		//
		String requestUri = request.getRequestURI();
		Matcher matcher = pattern.matcher(requestUri);
		if(matcher.find()) {
			String shortClassName = StringUtils.capitalize(matcher.group(1));
			String classPattern = "classpath:" + basePackage + shortClassName + ".class";
			Class<?> validationBeanClass = validationBeanClassFinder.findOne(classPattern);
			//
			if(validationBeanClass!=null) {
				generateJavaScript(request, validationBeanClass);
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
	private static final String TEMPLATE_VALIDATE = "/WEB-INF/templates/validate/ValidateOptionsTemplate.ftl";
	private void generateJavaScript(HttpServletRequest request, Class<?> clazz) throws IOException, TemplateException {
		ValidateBean validateBean = BeanValidationHelper.createForClass(validatorFactory, clazz);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("validateBean", validateBean);
		//
		Template t = freeMarkerConfigurer.getConfiguration().getTemplate(TEMPLATE_VALIDATE);
		String result = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		//
		String jsValidatePath = request.getSession().getServletContext().getRealPath("/WEB-INF/generate/js/validate");
		String jsValidateName = validateBean.getBeanName() + ".validate.js";
		File baseDir = new File(jsValidatePath);
		baseDir.mkdirs();
		//
		File out = new File(baseDir, jsValidateName);
		out.createNewFile();
		FileCopyUtils.copy(result.getBytes(), out);
	}
	
}
