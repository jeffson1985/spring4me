package org.osforce.spring4me.support.validation;

import java.util.Set;

import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-validation.xml")
public class ValidationTest {

	@Autowired
	private Validator validator;
	
	@Test
	public void testValidate() {
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass(User.class);
		Set<PropertyDescriptor> propertyDescriptors = beanDescriptor.getConstrainedProperties();
		for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Set<ConstraintDescriptor<?>> constraintDesctipors = propertyDescriptor.getConstraintDescriptors();
			for(ConstraintDescriptor<?> constraintDescriptor : constraintDesctipors) {
				System.out.println(propertyDescriptor.getPropertyName() + " --- " + constraintDescriptor);
			}
		}
	}
	
}
