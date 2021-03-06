package org.sarons.spring4me.support.validation;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:53:10
 */
public class ValidateConstraint {

	private String constraintName;
	private String constraintValue;
	//
	private String message;

	public ValidateConstraint(String constraintName, String constraintValue) {
		this.constraintName = constraintName;
		this.constraintValue = constraintValue;
	}

	public String getConstraintName() {
		return constraintName;
	}

	public void setConstraintName(String constraintName) {
		this.constraintName = constraintName;
	}

	public String getConstraintValue() {
		if(constraintValue!=null && constraintValue.contains(",")) {
			return "[" +constraintValue+ "]";
		}
		return constraintValue;
	}

	public void setConstraintValue(String constraintValue) {
		this.constraintValue = constraintValue;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
