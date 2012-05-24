<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Spring4Me Demo</title>
        <style type="text/css">
		.widget {
			border: 1px solid #ddd;
			min-height: 100px;
			margin-bottom: 10px;
		}
		.widget-head {
			background: #eee;
			padding: 5px;
		}
		.widget-head h3 {
			margin-top:10px;
			margin-bottom:10px;
			line-height:0;
		}
		.widget-body {
			padding: 5px;
		}
		
		table.widget_container {
			width: 100%;
		}
		table.widget_container td {
			vertical-align: top;
		}
		table.widget_container td.left {
			width:20%;
		} 
		table.widget_container td.main {
		} 
		table.widget_container td.right {
			width:30%;
		} 
		</style>
    </head>
    <body>
        <table class="widget_container">
        	<tr height="20%">
        		<td colspan="3"><@placeholder groupId="top-side" /></td>
        	</tr>
            <tr height="70%">
            	<td class="left"><@placeholder groupId="left-side" /></td>
                <td class="main"><@placeholder groupId="main-body" /></td>
                <td class="right"><@placeholder groupId="right-side" /></td>
            </tr>
            <tr height="10%">
        		<td colspan="3"><@placeholder groupId="bottom-side" /></td>
        	</tr>
        </table>
    </body>
</html>
