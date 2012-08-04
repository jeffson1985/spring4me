package org.sarons.spring4me.support.validation;

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
 * @create 2012-7-30 - 下午9:52:56
 */
public abstract class BeanValidateHelper {
	
	/**
	 * 根据 bean class 及 分组 获取当前分组的  校验元数据
	 * @param validatorFactory
	 * @param clazz
	 * @param groups
	 * @return
	 */
	public static ValidateBean createForClass(ValidatorFactory validatorFactory, Class<?> clazz, Class<?>...groups) {
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
				// groups checks
				if(isOutOfGroup(constraintDescriptor, groups)) {
					continue;
				}
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
			if(!validateProperty.getValidateConstraints().isEmpty()) {
				validateBean.addValidateProperty(validateProperty);
			}
		}
		//
		return validateBean;
	}
	
	/**
	 * 判断 constraintDescriptor 是否在给定的分组范围之内
	 * @param constraintDescriptor
	 * @param groups
	 * @return
	 */
	private static boolean isOutOfGroup(ConstraintDescriptor<?> constraintDescriptor, Class<?>...groups) {
		boolean outOfFlag = true;
		for(Class<?> group : groups) {
			for(Class<?> target : constraintDescriptor.getGroups()) {
				if(ClassUtils.isAssignable(group, target)) {
					outOfFlag = false;
				}
			}
		}
		return outOfFlag;
	}

	/**
	 * 获取约束名称
	 * @param constraintDescriptor
	 * @return
	 */
	private static String getConstraintName(ConstraintDescriptor<?> constraintDescriptor) {
		return constraintDescriptor.getAnnotation().annotationType().getSimpleName().toLowerCase();
	}

	/**
	 * 获取约束
	 * @param constraintDescriptor
	 * @return
	 */
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
