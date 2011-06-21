package org.osforce.spring4me.commons.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.osforce.spring4me.commons.collection.CollectionUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:38:35 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class XMLUtil {

	private static DocumentBuilder docBuilder;

	static {
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static Document parse(InputStream is) {
		try {
			return docBuilder.parse(is);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document parse(File file) {
		try {
			return docBuilder.parse(file);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Document parse(String xmlStr) {
		try {
			return docBuilder.parse(new ByteArrayInputStream(xmlStr.getBytes()));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Element parseToElement(File file) {
		return parse(file).getDocumentElement();
	}

	public static Element parseToElement(String xmlStr) {
		return parse(xmlStr).getDocumentElement();
	}

	public static List<Element> selectElements(Element element, String tagName) {
		List<Element> elementList = CollectionUtil.newArrayList();
		NodeList nodeList = null;
		if(tagName==null) {
			nodeList = element.getChildNodes();
		} else {
			nodeList = element.getElementsByTagName(tagName);
		}
		for(int i=0;i<nodeList.getLength();i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				elementList.add((Element)node);
			}
		}
		return elementList;
	}

	public static List<Element> selectElements(Document xmlDoc, String xpath) {
		List<Element> elements = CollectionUtil.newArrayList();
		try {
			Object result = XPathFactory.newInstance().newXPath().evaluate(
					xpath, xmlDoc, XPathConstants.NODESET);
			NodeList nodeList = (NodeList) result;
			for(int i=0;i<nodeList.getLength();i++) {
				elements.add((Element)nodeList.item(i));
			}
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return elements;
	}

	public static Element selectSingleElement(Element element, String xpath) {
		try {
			return (Element) XPathFactory.newInstance().newXPath().evaluate(
					xpath, element, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Element> elements(Element element) {
		return selectElements(element, null);
	}

	public static List<Attr> attributes(Element element) {
		List<Attr> attrList = CollectionUtil.newArrayList();
		NamedNodeMap attributes = element.getAttributes();
		for(int i=0;i<attributes.getLength();i++) {
			Node node = attributes.item(i);
			if(node.getNodeType()==Node.ATTRIBUTE_NODE) {
				attrList.add((Attr)node);
			}
		}
		return attrList;
	}

	public static Document createDocument() {
		return docBuilder.newDocument();
	}

	public static String toString(Document xmlDoc) {
		StringWriter out = new StringWriter();
		try {
			TransformerFactory.newInstance().newTransformer().transform(
			        new DOMSource(xmlDoc), new StreamResult(out));
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		}
		return out.toString();
	}

	public static Element getElementByTagName(Element element, String tagName) {
		NodeList elementList = element.getElementsByTagName(tagName);
		if(elementList.getLength()==1) {
			return (Element) elementList.item(0);
		} else {
			NodeList nodeList = element.getChildNodes();
			for(int i=0;i<nodeList.getLength();i++) {
				Node node = nodeList.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE) {
					Element targetEle = getElementByTagName(element, tagName);
					if(targetEle!=null) {
						return targetEle;
					}
				}
			}
		}
		return null;
	}

	public static Element getRootElement(Document xmlDoc) {
		return xmlDoc.getDocumentElement();
	}

	public static List<Element> getChildElements(Element element) {
		List<Element> childEles = CollectionUtil.newArrayList();
		NodeList nodeList = element.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(Node.ELEMENT_NODE==node.getNodeType()) {
				childEles.add((Element)node);
			}
		}
		return childEles;
	}

	public static Element getChildElement(Element  element, String tagName) {
		NodeList nodeList = element.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(Node.ELEMENT_NODE==node.getNodeType() &&
					tagName.equals(node.getNodeName())) {
				return (Element)node;
			}
		}
		return null;
	}

	public static List<Element> getChildElements(Element  element, String tagName) {
		List<Element> childEles = CollectionUtil.newArrayList();
		NodeList nodeList = element.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(Node.ELEMENT_NODE==node.getNodeType() &&
					tagName.equals(node.getNodeName())) {
				childEles.add((Element)node);
			}
		}
		return childEles;
	}

	public static String getAttribute(Element element, String attrName) {
		return element.getAttribute(attrName);
	}

	public static Map<String, String> getAttributes(Element element) {
		Map<String, String> attributes = CollectionUtil.newHashMap();
		NamedNodeMap attrMap = element.getAttributes();
		for(int i=0; i<attrMap.getLength(); i++) {
			Node attr = attrMap.item(i);
			if(attr.getNodeType()==Node.ATTRIBUTE_NODE) {
				String name = ((Attr)attr).getName();
				String value = ((Attr)attr).getValue();
				attributes.put(name, value);
			}
		}
		return attributes;
	}

	public static String getName(Element element) {
		return element.getTagName();
	}

	public static String getValue(Element element) {
		return element.getTextContent();
	}
	
	public static Boolean hasAttribute(Element element, String attrName) {
		return element.hasAttribute(attrName);
	}
	
	public static Boolean hasAttributeValue(Element element, String attrName, String attrValue) {
		if(element.hasAttribute(attrName) && 
				attrValue.equals(getAttribute(element, attrName))) {
			return true;
		}
		return false;
	}
}
