var ${validateBean.beanName}ValidateOptions = {
	<#if validateBean.validateProperties?size gt 0>
	rules:{
		<#list validateBean.validateProperties as validateProperty>
		${validateProperty.propertyName}: {
			<#list validateProperty.validateConstraints as validateConstraint>
			${validateConstraint.constraintName}:<#if validateConstraint.constraintValue==''>true<#else>'${validateConstraint.constraintValue}'</#if><#rt>
			<#lt><#if validateConstraint_has_next>,</#if>
			</#list>
		}<#if validateProperty_has_next>,</#if>
		</#list>
	},
	messages: {
		<#list validateBean.validateProperties as validateProperty>
		${validateProperty.propertyName}: {
			<#list validateProperty.validateConstraints as validateConstraint>
			${validateConstraint.constraintName}:'${validateConstraint.message}'<#rt>
			<#lt><#if validateConstraint_has_next>,</#if>
			</#list>
		}<#if validateProperty_has_next>,</#if>
		</#list>
	}
	</#if>
};