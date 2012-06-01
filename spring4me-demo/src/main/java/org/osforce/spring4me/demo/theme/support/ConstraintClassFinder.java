package org.osforce.spring4me.demo.theme.support;

public class ConstraintClassFinder extends AbstractClassFinder {
	
	private static final String[] DEFAULT_CONSTRAINT_PATTERNS = new String[]{
		"classpath:/javax/validation/constraints/*.class",
		"classpath:/org/hibernate/validator/constraints/*.class"
	};
	
	public ConstraintClassFinder() {
		super(DEFAULT_CONSTRAINT_PATTERNS);
	}
	
}
