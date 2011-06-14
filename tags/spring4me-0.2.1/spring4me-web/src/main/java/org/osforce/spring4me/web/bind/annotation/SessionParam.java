package org.osforce.spring4me.web.bind.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author <a href="mailto:haozhonghu@hotmail.com">gavin</a>
 * @since 0.1.1
 * @create Jun 3, 2011 - 2:37:38 PM
 * <a href="http://www.opensourceforce.org">开源力量</a>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionParam {
	
	String value() default "";
	
}
