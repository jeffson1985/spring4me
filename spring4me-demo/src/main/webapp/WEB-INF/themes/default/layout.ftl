<#assign w=JspTaglibs["/WEB-INF/tld/widget.tld"]>

<#assign base = "/spring4me-demo" />

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Default Theme</title>
	<script type="text/javascript" src="${base}/static/js/jquery.js"></script>
	<script type="text/javascript" src="${base}/static/js/jquery.layout.js"></script>
	<script type="text/javascript" src="${base}/static/js/jquery.ui.all.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function(){
		$('body').layout();
	});
	</script>
	
	<style type="text/css">
	/**
	 *	Basic Layout Theme
	 * 
	 *	This theme uses the default layout class-names for all classes
	 *	Add any 'custom class-names', from options: paneClass, resizerClass, togglerClass
	 */

	.ui-layout-pane { /* all 'panes' */ 
		background: #FFF; 
		border: 1px solid #BBB; 
		padding: 10px; 
		overflow: auto;
	} 

	.ui-layout-resizer { /* all 'resizer-bars' */ 
		background: #DDD; 
	} 

	.ui-layout-toggler { /* all 'toggler-buttons' */ 
		background: #AAA; 
	} 


	</style>
</head>
<body>
	<div class="ui-layout-north"></div>
	<div class="ui-layout-center"><@w.placeholder name="center"/></div>
	<div class="ui-layout-east"></div>
	<div class="ui-layout-west"></div>
	<div class="ui-layout-south"></div>
</body>
</html>