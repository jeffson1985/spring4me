<html>
	<head>
		<title>Layout Example</title>
		<script type="text/javascript" src="${base}/static/resources/js/jquery.js"></script>
		<script type="text/javascript" src="${base}/static/resources/js/jquery.validate.js"></script>
		<script type="text/javascript" src="${base}/static/resources/js/validate.additional.js"></script>
		<script type="text/javascript" src="${base}/static/resources/js/widget.loading.js"></script>
		<link type="text/css" rel="stylesheet" href="${base}/static/themes/default/css/layout.css">
	</head>
	<body>
		<header>
			<@placeholder groupId="top-side" />
	    </header>
	    <div class="clearboth">
		    <aside class="w15 left">
		    </aside>
		    <aside class="w15 right">
				<@placeholder groupId="right-side" />
	      	</aside>
	      	<section class="w70 center">
	      		<@placeholder groupId="main-body" />
	      	</section>
		</div>
	</body>
</html>