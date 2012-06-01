package org.osforce.spring4me.support.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.MessageInterpolator;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-6-1 - 下午3:10:03
 */
public abstract class BeanValidationHelper {
	
	public static ValidateBean createForClass(ValidatorFactory validatorFactory, Class<?> clazz) {
		Class<?> beanType = clazz;
		String beanName = StringUtils.uncapitalize(clazz.getSimpleName());
		//
		ValidateBean validateBean = new ValidateBean(beanName, beanType);
		//
		Validator validator = validatorFactory.getValidator();
		MessageInterpolator messageInterpolator = validatorFactory.getMessageInterpolator();
		//
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass(beanType);
		Set<PropertyDescriptor> propertyDescriptors = beanDescriptor.getConstrainedProperties();
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			String propertyName = propertyDescriptor.getPropertyName();
			ValidateProperty validateProperty = new ValidateProperty(propertyName);
			//
			Set<ConstraintDescriptor<?>> constraintDesctipors = propertyDescriptor.getConstraintDescriptors();
			for(final ConstraintDescriptor<?> constraintDescriptor : constraintDesctipors) {
				//
				String constraintName = getConstraintName(constraintDescriptor);
				String constraintValue = getConstraintValue(constraintDescriptor);
				ValidateConstraint validateConstraint = new ValidateConstraint(constraintName, constraintValue);
				//
				String messageTemplate = (String) constraintDescriptor.getAttributes().get("message");
				MessageInterpolator.Context context = new MessageInterpolatorContext(constraintDescriptor);
				String message = messageInterpolator.interpolate(messageTemplate, context);
				//
				validateConstraint.setMessage(message);
				validateProperty.addValidateConstraint(validateConstraint);
			}
			//
			validateBean.addValidateProperty(validateProperty);
		}
		//
		return validateBean;
	}
	
	private static String getConstraintName(ConstraintDescriptor<?> constraintDescriptor) {
		return constraintDescriptor.getAnnotation().annotationType().getSimpleName().toLowerCase();
	}

	private static String getConstraintValue(ConstraintDescriptor<?> constraintDescriptor) {
		Annotation constraintInstance = constraintDescriptor.getAnnotation();
		Class<?> constraintClass = constraintDescriptor.getAnnotation().annotationType();
		Method valueMethod = ClassUtils.getMethodIfAvailable(constraintClass, "value", (Class<?>[])null);
		if(valueMethod!=null) {
			return String.valueOf(ReflectionUtils.invokeMethod(valueMethod, constraintInstance));
		} else {
			List<String> values = new ArrayList<String>();
			Map<String, Object> annotationAttrs = AnnotationUtils.getAnnotationAttributes(constraintInstance);
			for(String key : annotationAttrs.keySet()) {
				if("message".equals(key) || "payload".equals(key) || "groups".equals(key)) {
					continue;
				}
				//
				values.add(String.valueOf(annotationAttrs.get(key)));
			}
			return StringUtils.collectionToCommaDelimitedString(values);
		}
	}

	private static class MessageInterpolatorContext implements MessageInterpolator.Context {
		
		private ConstraintDescriptor<?> constraintDescriptor;
		
		public MessageInterpolatorContext(ConstraintDescriptor<?> constraintDescriptor) {
			this.constraintDescriptor = constraintDescriptor;
		}

		public ConstraintDescriptor<?> getConstraintDescriptor() {
			return constraintDescriptor;
		}

		public Object getValidatedValue() {
			return null;
		}
		
	}
	
}
