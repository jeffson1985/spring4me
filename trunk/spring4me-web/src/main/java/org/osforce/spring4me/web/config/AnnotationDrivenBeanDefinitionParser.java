package org.osforce.spring4me.web.config;

import org.apache.commons.lang.StringUtils;
import org.osforce.spring4me.commons.xml.XMLUtil;
import org.osforce.spring4me.web.bind.support.WidgetCustomArgumentResolver;
import org.osforce.spring4me.web.widget.config.ConfigFactory;
import org.osforce.spring4me.web.widget.config.impl.DefaultConfigFactory;
import org.osforce.spring4me.web.widget.config.impl.XmlConfigParser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.feed.AtomFeedHttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.handler.ConversionServiceExposingInterceptor;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.0
 * @create May 13, 2011 - 4:37:16 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
public class AnnotationDrivenBeanDefinitionParser implements BeanDefinitionParser {

	private static final boolean jsr303Present = ClassUtils.isPresent(
			"javax.validation.Validator", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());

	private static final boolean jaxb2Present =
			ClassUtils.isPresent("javax.xml.bind.Binder", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());

	private static final boolean jacksonPresent =
			ClassUtils.isPresent("org.codehaus.jackson.map.ObjectMapper", AnnotationDrivenBeanDefinitionParser.class.getClassLoader()) &&
					ClassUtils.isPresent("org.codehaus.jackson.JsonGenerator", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());

	private static boolean romePresent =
			ClassUtils.isPresent("com.sun.syndication.feed.WireFeed", AnnotationDrivenBeanDefinitionParser.class.getClassLoader());


	public BeanDefinition parse(Element element, ParserContext parserContext) {
		Object source = parserContext.extractSource(element);

		CompositeComponentDefinition compDefinition = new CompositeComponentDefinition(element.getTagName(), source);
		parserContext.pushContainingComponent(compDefinition);
		
		RootBeanDefinition annMappingDef = new RootBeanDefinition(DefaultAnnotationHandlerMapping.class);
		annMappingDef.setSource(source);
		annMappingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		annMappingDef.getPropertyValues().add("order", 0);
		String annMappingName = parserContext.getReaderContext().registerWithGeneratedName(annMappingDef);

		RuntimeBeanReference conversionService = getConversionService(element, source, parserContext);
		RuntimeBeanReference validator = getValidator(element, source, parserContext);
		
		RootBeanDefinition bindingDef = new RootBeanDefinition(ConfigurableWebBindingInitializer.class);
		bindingDef.setSource(source);
		bindingDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		bindingDef.getPropertyValues().add("conversionService", conversionService);
		bindingDef.getPropertyValues().add("validator", validator);
		// custom argument resolvers
		ManagedList<RuntimeBeanReference> customArgumentResolverRefs = getCustomArgumentResolvers(element, source, parserContext, conversionService); 
		
		RootBeanDefinition annAdapterDef = new RootBeanDefinition(AnnotationMethodHandlerAdapter.class);
		annAdapterDef.setSource(source);
		annAdapterDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		annAdapterDef.getPropertyValues().add("webBindingInitializer", bindingDef);
		annAdapterDef.getPropertyValues().add("messageConverters", getMessageConverters(source));
		annAdapterDef.getPropertyValues().add("customArgumentResolvers", customArgumentResolverRefs);
		String annAdapterName = parserContext.getReaderContext().registerWithGeneratedName(annAdapterDef);

		RootBeanDefinition csInterceptorDef = new RootBeanDefinition(ConversionServiceExposingInterceptor.class);
		csInterceptorDef.setSource(source);
		csInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, conversionService);		
		RootBeanDefinition mappedCsInterceptorDef = new RootBeanDefinition(MappedInterceptor.class);
		mappedCsInterceptorDef.setSource(source);
		mappedCsInterceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		mappedCsInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(0, (Object) null);
		mappedCsInterceptorDef.getConstructorArgumentValues().addIndexedArgumentValue(1, csInterceptorDef);
		String mappedInterceptorName = parserContext.getReaderContext().registerWithGeneratedName(mappedCsInterceptorDef);
		
		//
		registerConfigFactory(element, source, parserContext);
		
		parserContext.registerComponent(new BeanComponentDefinition(annMappingDef, annMappingName));
		parserContext.registerComponent(new BeanComponentDefinition(annAdapterDef, annAdapterName));
		parserContext.registerComponent(new BeanComponentDefinition(mappedCsInterceptorDef, mappedInterceptorName));
		parserContext.popAndRegisterContainingComponent();
		
		return null;
	}
	

	private RuntimeBeanReference getConversionService(Element element, Object source, ParserContext parserContext) {
		if (element.hasAttribute("conversion-service")) {
			return new RuntimeBeanReference(element.getAttribute("conversion-service"));
		}
		else {
			RootBeanDefinition conversionDef = new RootBeanDefinition(FormattingConversionServiceFactoryBean.class);
			conversionDef.setSource(source);
			conversionDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			String conversionName = parserContext.getReaderContext().registerWithGeneratedName(conversionDef);
			parserContext.registerComponent(new BeanComponentDefinition(conversionDef, conversionName));
			return new RuntimeBeanReference(conversionName);
		}
	}

	private RuntimeBeanReference getValidator(Element element, Object source, ParserContext parserContext) {
		if (element.hasAttribute("validator")) {
			return new RuntimeBeanReference(element.getAttribute("validator"));
		}
		else if (jsr303Present) {
			RootBeanDefinition validatorDef = new RootBeanDefinition(LocalValidatorFactoryBean.class);
			validatorDef.setSource(source);
			validatorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			String validatorName = parserContext.getReaderContext().registerWithGeneratedName(validatorDef);
			parserContext.registerComponent(new BeanComponentDefinition(validatorDef, validatorName));
			return new RuntimeBeanReference(validatorName);
		}
		else {
			return null;
		}
	}
	
	private ManagedList<RuntimeBeanReference> getCustomArgumentResolvers(
			Element element, Object source, ParserContext parserContext, RuntimeBeanReference conversionService) {
		ManagedList<RuntimeBeanReference> refs = new ManagedList<RuntimeBeanReference>();
		//
		RootBeanDefinition argResolverDef = new RootBeanDefinition(WidgetCustomArgumentResolver.class);
		argResolverDef.setSource(source);
		argResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		argResolverDef.getPropertyValues().add("conversionService", conversionService);
		String argResolverName = parserContext.getReaderContext().registerWithGeneratedName(argResolverDef);
		refs.add(new RuntimeBeanReference(argResolverName));
		//
		if(element.hasAttribute("custom-argument-resolvers")) {
			String resolversStr = XMLUtil.getAttribute(element, "custom-argument-resolvers");
			String[] resolvers = StringUtils.split(resolversStr, ",");
			for(String resolver : resolvers) {
				if(StringUtils.isNotBlank(resolver)) {
					refs.add(new RuntimeBeanReference(resolver));
				}
			}
		}
		return refs;
	}
	
	private void registerConfigFactory(Element element, Object source, ParserContext parserContext) {
		if (!element.hasAttribute("config-factory")) {
			RootBeanDefinition configParserDef = new RootBeanDefinition(XmlConfigParser.class);
			configParserDef.setSource(source);
			configParserDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			//
			Boolean cache = true;
			if(XMLUtil.hasAttributeValue(element, "cache", "false")) {
				cache = false;
			}
			//
			RootBeanDefinition configFactoryDef = new RootBeanDefinition(DefaultConfigFactory.class);
			configFactoryDef.setSource(source);
			configFactoryDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			configFactoryDef.getPropertyValues().add("configParser", configParserDef);
			configFactoryDef.getPropertyValues().add("cache", cache);
			parserContext.getReaderContext().registerWithGeneratedName(configFactoryDef);
			parserContext.registerComponent(new BeanComponentDefinition(configFactoryDef, ConfigFactory.KEY));
		}
	}

	private ManagedList<RootBeanDefinition> getMessageConverters(Object source) {
		ManagedList<RootBeanDefinition> messageConverters = new ManagedList<RootBeanDefinition>();
		messageConverters.setSource(source);
		messageConverters.add(createConverterBeanDefinition(ByteArrayHttpMessageConverter.class, source));

		RootBeanDefinition stringConverterDef = createConverterBeanDefinition(StringHttpMessageConverter.class, source);
		stringConverterDef.getPropertyValues().add("writeAcceptCharset", false);
		messageConverters.add(stringConverterDef);

		messageConverters.add(createConverterBeanDefinition(ResourceHttpMessageConverter.class, source));
		messageConverters.add(createConverterBeanDefinition(SourceHttpMessageConverter.class, source));
		messageConverters.add(createConverterBeanDefinition(XmlAwareFormHttpMessageConverter.class, source));
		if (jaxb2Present) {
			messageConverters.add(createConverterBeanDefinition(Jaxb2RootElementHttpMessageConverter.class, source));
		}
		if (jacksonPresent) {
			messageConverters.add(createConverterBeanDefinition(MappingJacksonHttpMessageConverter.class, source));
		}
		if (romePresent) {
			messageConverters.add(createConverterBeanDefinition(AtomFeedHttpMessageConverter.class, source));
			messageConverters.add(createConverterBeanDefinition(RssChannelHttpMessageConverter.class, source));
		}
		return messageConverters;
	}

	@SuppressWarnings("unchecked")
	private RootBeanDefinition createConverterBeanDefinition(Class<? extends HttpMessageConverter> converterClass,
			Object source) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition(converterClass);
		beanDefinition.setSource(source);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

		return beanDefinition;
	}
}
