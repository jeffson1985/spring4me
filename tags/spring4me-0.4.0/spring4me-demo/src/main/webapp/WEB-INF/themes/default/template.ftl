<html>
	<head>
		<title>Layout Example</title>
		<script type="text/javascript" src="${base}/resources/js/jquery.js"></script>
		<script type="text/javascript" src="${base}/generate/js/${PageConfig.path}.js"></script>
		<script type="text/javascript" src="${base}/themes/default/js/jquery.layout.js"></script>
		<script type="text/javascript" src="${base}/themes/default/js/layout.default.js"></script>
		<link type="text/css" rel="stylesheet" href="${base}/themes/default/css/style.css">
	</head>
	<body>
		<div id="container">
			<div class="ui-layout-center">
				<@placeholder groupId="main-body" />
			</div>
			<div class="ui-layout-north">
				<@placeholder groupId="top-side" />
			</div>
			<div class="ui-layout-east">
				<@placeholder groupId="right-side" />
			</div>
			<div class="ui-layout-west">
				<@placeholder groupId="left-side" />
			</div>
		</div>
	</body>
</HTML>