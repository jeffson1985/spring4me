package org.osforce.spring4me.web.flow.config.xml;

import java.util.List;
import java.util.Set;

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
			return parseFlow(flowEle);
		} catch (Exception e) {
			throw new FlowNotFoundException(e.getMessage(), e.getCause());
		}
	}
	
	public FlowConfig findFlow(String from, String to) {
		try {
			from = (from==null ? "":from);
			to = (to==null ? "":to);
			//
			Element flowEle = findFlowElement(null, from, to);
			//
			return parseFlow(flowEle);
		} catch (Exception e) {
			throw new FlowNotFoundException("Flow is not found, please check flow configuration!", e);
		}
	}
	
	public FlowConfig findSubFlow(String name, String from, String to) {
		try {
			FlowConfig flowConfig = findFlow(name);
			StepConfig stepFrom = flowConfig.getStepConfig(from);
			Set<String> subFlowNames = stepFrom.getSubFlowNames();
			for(String subFlowName : subFlowNames) {
				try {
					Element flowEle = findFlowElement(subFlowName, null, to);
					return parseFlow(flowEle);
				} catch (NullPointerException e) {
					// skip
				}
			}
			//
			throw new NullPointerException();
		} catch (Exception e) {
			throw new FlowNotFoundException("SubFlow is not found, please check flow configuration!", e);
		}
	}
	
	private Element findFlowElement(String name, String from, String to) throws Exception {
		//
		for(Resource configLocation : configLocations) {
			if(configLocation.isReadable()) {
				Document document = xmlParser.parseAndValidate(configLocation);
				List<Element> flowEles = DomUtils.getChildElements(document.getDocumentElement());
				for(Element flowEle : flowEles) {
					if(StringUtils.hasText(name)) {
						if(name.equals(flowEle.getAttribute("name"))) {
							//
							if(StringUtils.hasText(to)) {
								Element stepEle = DomUtils.getChildElements(flowEle).get(0);
								if(to.equals(stepEle.getAttribute("page"))) {
									return flowEle;
								}
								//
								continue;
							}
							//
							return flowEle;
						}
					} else {
						List<Element> stepEles = DomUtils.getChildElements(flowEle);
						for(int i=0, len=stepEles.size(); i<len-1; i++) {
							Element stepEle1 = stepEles.get(i);
							Element stepEle2 = stepEles.get(i+1);
							//
							if(!from.equals(stepEle1.getAttribute("page"))
									&& to.equals(stepEle1.getAttribute("page"))
									&& "start".equals(stepEle1.getNodeName())) {
								return flowEle;
							}
							//
							if(from.equals(stepEle1.getAttribute("page"))
									&& to.equals(stepEle2.getAttribute("page"))) {
								return flowEle;
							}
							//
							if(from.equals(stepEle2.getAttribute("page"))
									&& !to.equals(stepEle2.getAttribute("page"))
									&& "finish".equals(stepEle2.getNodeName())) {
								return flowEle;
							}
						}
					}
				}
			}
		}
		//
		throw new NullPointerException("Flow element not found!");
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
					stepConfig.addSubFlow(name);
				}
			}
			//
			flowConfig.addStepConfig(stepConfig);
		}
		//
		return flowConfig;
	}

}
