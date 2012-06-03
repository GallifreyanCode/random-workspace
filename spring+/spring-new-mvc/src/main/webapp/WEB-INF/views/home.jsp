<head>
</head>
<body>
<h1>
	Home
</h1>

<h2>Those are all known users</h2>
<c:forEach var="user" items="${users}">
<p>${user.name}</p>
</c:forEach>
</body>
</html>
