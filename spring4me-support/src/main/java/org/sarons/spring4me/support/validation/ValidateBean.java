package org.sarons.spring4me.support.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:53:02
 */
public class ValidateBean {

	private String beanName;
	private Class<?> beanType;
	
	private List<ValidateProperty> validateProperties = new ArrayList<ValidateProperty>();

	public ValidateBean(String beanName, Class<?> beanType) {
		this.beanName = beanName;
		this.beanType = beanType;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	
	public String getBeanClassName() {
		return this.beanType.getName();
	}
	
	public String getBeanClassSimpleName() {
		return this.beanType.getSimpleName();
	}
 
	public Class<?> getBeanType() {
		return beanType;
	}

	public void setBeanType(Class<?> beanType) {
		this.beanType = beanType;
	}
	
	public List<ValidateProperty> getValidateProperties() {
		return validateProperties;
	}
	
	public void addValidateProperty(ValidateProperty validateProperty) {
		this.validateProperties.add(validateProperty);
	}
	
	public void setValidateProperties(List<ValidateProperty> validateProperties) {
		this.validateProperties = validateProperties;
	}
	
}
