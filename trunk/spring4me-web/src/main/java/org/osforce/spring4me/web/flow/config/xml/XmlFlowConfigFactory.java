package org.osforce.spring4me.web.flow.config.xml;

import java.util.List;

import org.osforce.spring4me.support.xml.XmlParser;
import org.osforce.spring4me.web.flow.FlowNotFoundException;
import org.osforce.spring4me.web.flow.config.AbstractFlowConfigFactory;
import org.osforce.spring4me.web.flow.config.FlowConfig;
import org.osforce.spring4me.web.flow.config.StepConfig;
import org.osforce.spring4me.web.flow.config.StepConfig.Type;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-5-29 - 下午2:16:25
 */
public class XmlFlowConfigFactory extends AbstractFlowConfigFactory {

	private Resource[] configLocations = new Resource[0];
	//
	private XmlParser xmlParser = new XmlParser();
	
	public void setConfigLocation(Resource configLocation) {
		this.configLocations = new Resource[]{configLocation};
	}
	
	public void setConfigLocations(Resource[] configLocations) {
		this.configLocations = configLocations;
	}
	
	@Override
	protected FlowConfig loadFlow(String name) {
		try {
			Element flowEle = findFlowElement(name, null, null);
			//
			if(flowEle==null) {
				throw new FlowNotFoundException(
						String.format("Flow %s not found, please check flow configuration!", name));
			}
			//
			return parseFlow(flowEle);
		} catch (Exception e) {
			throw new FlowNotFoundException(e.getMessage(), e.getCause());
		}
	}
	
	public FlowConfig findFlow(String step1, String step2) {
		try {
			step1 = (step1==null ? "":step1);
			step2 = (step2==null ? "":step2);
			//
			Element flowEle = findFlowElement(null, step1, step2);
			if(flowEle==null) {
				throw new FlowNotFoundException(
						String.format("Flow is not found, please check flow configuration!"));
			}
			//
			return parseFlow(flowEle);
		} catch (Exception e) {
			throw new FlowNotFoundException("Flow is not found, please check flow configuration!", e);
		}
	}
	
	private Element findFlowElement(String name, String step1, String step2) throws Exception {
		Element targetEle = null;
		//
		outer:
		for(Resource configLocation : configLocations) {
			if(configLocation.isReadable()) {
				Document document = xmlParser.parseAndValidate(configLocation);
				List<Element> flowEles = DomUtils.getChildElements(document.getDocumentElement());
				for(Element flowEle : flowEles) {
					if(StringUtils.hasText(name)) {
						if(name.equals(flowEle.getAttribute("name"))) {
							targetEle = flowEle;
							break outer;
						}
					} else {
						List<Element> stepEles = DomUtils.getChildElements(flowEle);
						for(int i=0, len=stepEles.size(); i<len-1; i++) {
							Element stepEle1 = stepEles.get(i);
							Element stepEle2 = stepEles.get(i+1);
							//
							if(!step1.equals(stepEle1.getAttribute("page"))
									&& step2.equals(stepEle1.getAttribute("page"))
									&& "start".equals(stepEle1.getNodeName())) {
								targetEle = flowEle;
								break outer;
							}
							//
							if(step1.equals(stepEle1.getAttribute("page"))
									&& step2.equals(stepEle2.getAttribute("page"))) {
								targetEle = flowEle;
								break outer;
							}
							//
							if(step1.equals(stepEle2.getAttribute("page"))
									&& !step2.equals(stepEle2.getAttribute("page"))
									&& "finish".equals(stepEle2.getNodeName())) {
								targetEle = flowEle;
								break outer;
							}
						}
					}
				}
			}
		}
		//
		return targetEle;
	}
	
	private FlowConfig parseFlow(Element flowEle) {
		String flowName = flowEle.getAttribute("name");
		FlowConfig flowConfig = new FlowConfig(flowName);
		//
		List<Element> stepEles = DomUtils.getChildElements(flowEle);
		for(Element stepEle : stepEles) {
			Type type = Type.step;
			String typeStr = stepEle.getNodeName();
			if("start".equals(typeStr) || "finish".equals(typeStr)) {
				type = Enum.valueOf(StepConfig.Type.class, typeStr);
			}
			//
			String page = stepEle.getAttribute("page");
			//
			StepConfig stepConfig = new StepConfig(type, page);
			//
			if(stepEle.hasChildNodes()) {
				List<Element> subflowEles = DomUtils.getChildElements(stepEle);
				for(Element subflowEle : subflowEles) {
					String name = subflowEle.getAttribute("name");
					FlowConfig subFlowConfig = findFlow(name);
					//
					stepConfig.addSubFlowConfig(subFlowConfig);
				}
			}
			//
			flowConfig.addStepConfig(stepConfig);
		}
		//
		return flowConfig;
	}

}
