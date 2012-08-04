package org.sarons.spring4me.web.servlet.view.freemarker;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sarons.spring4me.web.page.tag.ftl.PlaceholderDirectiveModel;

import freemarker.template.SimpleHash;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:50:33
 */
public class FreeMarkerView extends org.springframework.web.servlet.view.freemarker.FreeMarkerView  {

	@Override
	protected SimpleHash buildTemplateModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		SimpleHash simpleHash = super.buildTemplateModel(model, request, response);
		simpleHash.put(PlaceholderDirectiveModel.DIRECTIVE_NAME, new PlaceholderDirectiveModel());
		//
		return simpleHash;
	}
	
}
