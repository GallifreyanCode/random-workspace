<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
<%@ page isELIgnored="false" %>
<style type="text/css">
.error {
	color: #ff0000;
}
</style>
<title>Create new User</title>
</head>
<body>
<form:form method="POST" commandName="newUserCommand">
		<table>
			<tr>
				<td>Name :</td>
				<td><form:textarea path="name" />
				</td>
				<td><form:errors path="name" cssClass="error" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>