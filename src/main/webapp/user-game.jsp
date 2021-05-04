<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Connect Five</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
</head>
<body>
	<h1>Connect Five</h1>
	<form method="post" action="new">
		<table>
			<tr>
				<td><label for="name">Name:</label></td>
				<td><input type="text" name="name"></td>
			</tr>
			<tr>
				<td><label for="colour">Choose a Color:</label></td>
				<td><select id="colour" name="colour">
						<option value="red">Red</option>
						<option value="yellow">Yellow</option>
				</select></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit"></td>
			</tr>

		</table>
	</form>
</body>
</html>