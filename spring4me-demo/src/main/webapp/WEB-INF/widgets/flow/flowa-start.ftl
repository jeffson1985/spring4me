<#import "/WEB-INF/spring.ftl" as spring />

<div class="widget">
    <div class="widget-head">
    	<h3>${widgetConfig.title}</h3>
    </div>
    <div class="widget-body">
   		<@spring.bind "flowaBean"/>
   		<form action='<@navigation event="process-flow" action="/flowa/process"/>' method="post">
   			<table>
   				<tr>
   					<td>流程名：</td>
   					<td>
   						<@spring.formInput path="flowaBean.name" />
						<@spring.showErrors separator="<br/>" classOrStyle="error"/>
					</td>
   				</tr>
   				<tr>
   					<td>额外信息：</td>
   					<td>
   						<@spring.formTextarea path="flowaBean.extras" />
						<@spring.showErrors separator="<br/>" classOrStyle="error"/>
					</td>
   				</tr>
   				<tr>
   					<td>
   						<input type="hidden" name="flowId" value="${flowId}" />
   					</td>
   					<td><input type="submit" value="Submit" /></td>
   				</tr>
   			</table>
   		</form>
    </div>
    <div class="widget-foot">
    </div>
</div>
