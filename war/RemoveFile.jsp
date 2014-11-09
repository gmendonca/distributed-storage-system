<!doctype html>
<%@ page import="agora.vai.server.BucketControl"%>

<%
BucketControl bc = new BucketControl("gaedistributedsystem.appspot.com");
String fileName = "teste";
Boolean result = false;
if(request.getParameter("description") != null){
	fileName = request.getParameter("description");
	result = bc.removeFile(fileName);
}
%>

<html>
  <head>
    <title>Distributed Storage System</title>
	</head>  
  <body>

    <h1>Distributed Storage System</h1>

<%
	if(result) {
%>
	<p><%= fileName %> Removed!</p>
<%
	}
%>

	<br><br><br>
	<form action="Teste.jsp" >
    	<input type="submit" value="Go Back"/>
	</form>

  </body>
</html>
