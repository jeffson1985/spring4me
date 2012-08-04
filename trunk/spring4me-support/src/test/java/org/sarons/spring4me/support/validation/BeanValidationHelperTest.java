package org.sarons.spring4me.support.validation;

import javax.validation.ValidatorFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sarons.spring4me.support.validation.BeanValidateHelper;
import org.sarons.spring4me.support.validation.ValidateBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/applicationContext-support.xml")
public class BeanValidationHelperTest {

	@Autowired
	private ValidatorFactory validatorFactory;
	
	@Test
	public void testHelper() {
		ValidateBean validateBean = BeanValidateHelper.createForClass(validatorFactory, User.class, UserChecks.class);
		System.out.println(validateBean.getBeanName());
	}
	
}
