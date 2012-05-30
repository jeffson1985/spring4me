<#import "/WEB-INF/spring.ftl" as spring />

<div class="widget">
    <div class="widget-head">
    	<h3>${widgetConfig.title}</h3>
    </div>
    <div class="widget-body">
   		<@spring.bind "flowBean"/>
   		<form action='<@navigation event="process-flow" action="${base}/flow/process"/>' method="post">
   			<label >流程名：</label>
   			<@spring.formInput path="flowBean.name" />
			<@spring.showErrors separator="<br/>" classOrStyle="error"/>
			<br/>
			<label >额外信息：</label>
   			<@spring.formTextarea path="flowBean.extras" />
			<@spring.showErrors separator="<br/>" classOrStyle="error"/>
			<br />
			<input type="submit" value="Submit" />
   		</form>
    </div>
    <div class="widget-foot">
    	<a href='<@navigation event="next-step" />'>Next Step!</a>
    </div>
</div>
