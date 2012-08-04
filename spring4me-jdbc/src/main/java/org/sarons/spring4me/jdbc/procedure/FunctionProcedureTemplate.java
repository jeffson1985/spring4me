package org.sarons.spring4me.jdbc.procedure;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午9:55:19
 */
public abstract class FunctionProcedureTemplate extends GenericProcedureTemplate {

	public FunctionProcedureTemplate() {
		//
		super();
		//
		setFunction(true);
	}

}
