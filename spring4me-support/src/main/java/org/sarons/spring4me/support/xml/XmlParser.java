package org.sarons.spring4me.support.xml;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.PluggableSchemaResolver;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:53:25
 */
public final class XmlParser {
	
	private static final Log log = LogFactory.getLog(XmlParser.class);
	
	public static final String DEFAULT_SCHEMA_MAPPINGS_LOCATION = "META-INF/spring4me.schemas";

	private DocumentBuilderFactory documentBuildFactory = DocumentBuilderFactory.newInstance();
	//
	private DocumentLoader documentLoader = new DefaultDocumentLoader();
	
	private ErrorHandler errorHandler = new SimpleSaxErrorHandler(log);
	
	private XmlValidationModeDetector validationModeDetector = new XmlValidationModeDetector();
	
	private EntityResolver entityResolver; 

	public XmlParser() {
		this(ClassUtils.getDefaultClassLoader());
	}
	
	public XmlParser(ClassLoader classLoader) {
		this.entityResolver = new PluggableSchemaResolver(classLoader, DEFAULT_SCHEMA_MAPPINGS_LOCATION);
	}
	
	public Document parse(Resource resource) throws Exception {
		return documentBuildFactory.newDocumentBuilder().parse(resource.getInputStream());
	}
	
	public Document parseAndValidate(Resource resource) throws Exception {
		InputSource inputSource = new InputSource(resource.getInputStream());
		int validationMode = validationModeDetector.detectValidationMode(resource.getInputStream());
		return documentLoader.loadDocument(inputSource, entityResolver, errorHandler, validationMode, false);
	}

}
