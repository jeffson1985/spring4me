package org.sarons.spring4me.support.validation;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author <a href="mailto:huhz@hundsun.com">Gavin Hu</a>
 * @create 2012-7-11 - 上午9:50:21
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:/applicationContext-support.xml")
public class BeanValidatorTest {

	@Autowired
	private Validator validator;
	
	@Test
	public void testValidate() {
		User user = new User();
		user.setEmail("gavin");
		Set<ConstraintViolation<User>> violations = validator.validate(user, Default.class, UserChecks.class);
		for(ConstraintViolation<User> violation : violations) {
			System.out.println(violation.getMessage());
		}
	}
	
}
