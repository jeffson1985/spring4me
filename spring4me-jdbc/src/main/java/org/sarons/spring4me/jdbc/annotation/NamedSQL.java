package org.sarons.spring4me.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:53:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedSQL {

	String value();
}
