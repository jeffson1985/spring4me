<div class="widget">
    <div class="widget-head">
    	<h3>${widgetConfig.title}</h3>
    </div>
    <div class="widget-body">
   		${flowBean.name}
   		<br/>
   		${flowBean.extras}
    </div>
    <div class="widget-foot">
    	<a href='<@navigation event="prev-step" />'>Pre Step!</a>
    	<a href='<@navigation event="next-step" />'>Next Step!</a>
		<a href='<@navigation event="subflow" />'>Subflow</a>
    </div>
</div>
