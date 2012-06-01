<div class="widget">
    <div class="widget-head">
    	<h3>${widgetConfig.title}</h3>
    </div>
    <div class="widget-body">
   		${widgetConfig.description}
    </div>
    <div class="widget-foot">
    	<a href='<@navigation event="prev-step" />&flowId=${flowId}'>Pre Step!</a>
    	<a href='<@navigation event="next-step" />&flowId=${flowId}'>Next Step!</a>
    </div>
</div>