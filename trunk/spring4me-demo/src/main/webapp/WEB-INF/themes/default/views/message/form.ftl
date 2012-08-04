<#import "/WEB-INF/themes/default/layout.ftl" as layout>

<@layout.main>
<form action="${base}/message/view" method="post">
	<table>
		<tr>
			<td>Name</td>
			<td><@include_page path="/component/name?value=${message.name!''}" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><@include_page path="/component/email?value=${message.email!''}" /></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><@include_page path="/component/title?value=${message.title!''}" /></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><@include_page path="/component/content?value=${message.content!''}" /></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><@include_page path="/component/name?value=${message.name!''}" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><@include_page path="/component/email?value=${message.email!''}" /></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><@include_page path="/component/title?value=${message.title!''}" /></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><@include_page path="/component/content?value=${message.content!''}" /></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><@include_page path="/component/name?value=${message.name!''}" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><@include_page path="/component/email?value=${message.email!''}" /></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><@include_page path="/component/title?value=${message.title!''}" /></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><@include_page path="/component/content?value=${message.content!''}" /></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><@include_page path="/component/name?value=${message.name!''}" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><@include_page path="/component/email?value=${message.email!''}" /></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><@include_page path="/component/title?value=${message.title!''}" /></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><@include_page path="/component/content?value=${message.content!''}" /></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><@include_page path="/component/name?value=${message.name!''}" /></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><@include_page path="/component/email?value=${message.email!''}" /></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><@include_page path="/component/title?value=${message.title!''}" /></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><@include_page path="/component/content?value=${message.content!''}" /></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Submit"></td>
		</tr>
	</table>
</form>
</@layout.main>