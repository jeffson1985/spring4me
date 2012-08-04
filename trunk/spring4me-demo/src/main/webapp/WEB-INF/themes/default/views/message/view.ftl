<#import "/WEB-INF/themes/default/layout.ftl" as layout>

<@layout.main>
<form action="${base}/message/record" method="post">
	<table>
		<tr>
			<td>Name</td>
			<td><input name="name" value="${message.name!''}"></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" value="${message.email!''}"></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><input name="title" value="${message.title!''}"></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><input name="content" value="${message.content!''}"></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><input name="name" value="${message.name!''}"></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" value="${message.email!''}"></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><input name="title" value="${message.title!''}"></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><input name="content" value="${message.content!''}"></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><input name="name" value="${message.name!''}"></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" value="${message.email!''}"></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><input name="title" value="${message.title!''}"></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><input name="content" value="${message.content!''}"></td>
		</tr>
		<tr>
			<td>Name</td>
			<td><input name="name" value="${message.name!''}"></td>
		</tr>
		<tr>
			<td>Email</td>
			<td><input name="email" value="${message.email!''}"></td>
		</tr>
		<tr>
			<td>Title</td>
			<td><input name="title" value="${message.title!''}"></td>
		</tr>
		<tr>
			<td>Content</td>
			<td><input name="content" value="${message.content!''}"></td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Submit"></td>
		</tr>
	</table>
</form>
</@layout.main>