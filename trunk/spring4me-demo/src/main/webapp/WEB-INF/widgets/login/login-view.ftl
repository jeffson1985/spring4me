<div class="widget">
    <div class="widget-head">
    	<h3>${widgetConfig.title}</h3>
    </div>
    <div class="widget-body">
   		<form id="loginForm" method="POST" action='<@navigation event="do-login" action="/login.do" />'>
   			<label>账号：</label>
   			<input name="username"/>
   			<br/>
   			<label>密码：</label>
   			<input type="password" name="password" />
   			<br/>
   			<input id="submit" type="submit" value="登录" />
   		</form>
    </div>
    <div class="widget-foot">
    	
    </div>
</div>
