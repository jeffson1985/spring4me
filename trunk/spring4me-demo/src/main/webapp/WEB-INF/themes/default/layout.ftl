<#--
 - layout
 - @author gavin
 - @create 2012/7/27
 -->

<#macro head Title="" Keywords="" Description="">
	<title>${Title}</title>
	<meta name="Keywords" content="${Keywords}" />
	<meta name="Description" content="${Description}" />
	<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/resources/js/jquery.validate.js"></script>
	<script type="text/javascript" src="${base}/resources/js/validate.additional.js"></script>
	<link type="text/css" rel="stylesheet" href="${base}/themes/default/css/layout.css">
</#macro>

<#macro main Title="" Keywords="" Description="">
	<@head Title="${Title}" Keywords="${Keywords}" Description="${Description}" />
	<body>
		<header>
			header
	    </header>
	    <div class="clearboth">
		    <aside class="w30 left">
		    	left
		    </aside>
	      	<section class="w70 center">
	      		<#nested />
	      	</section>
		</div>
	</body>
</#macro>