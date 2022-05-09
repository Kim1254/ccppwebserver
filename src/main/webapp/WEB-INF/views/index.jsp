<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Title</title>
	</head>
	<body>
		<script>
			var HOST = location.origin.replace(/^http/, 'ws') + '/ws';
			var ws = new WebSocket(HOST);
		</script>
	</body>
</html>
