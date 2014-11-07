<!doctype html>
<%@ page import="agora.vai.server.BucketControl"%>

<%
BucketControl bc = new BucketControl("gaedistributedsystem.appspot.com");
String fileName = "teste";
byte[] bytes = new byte[1024];
if(request.getParameter("description") != null){
	fileName = request.getParameter("description");
	bytes = bc.readFromFile(fileName);
}
%>

<html>
  <head>
    <title>Distributed Storage System</title>
    
  <body>

    <h1>Distributed Storage System</h1>
    
<%
	String str = new String(bytes, "UTF-8");
%>
	<p><%= str %></p>


	<br><br><br>
	<form action="Teste.jsp" >
    	<input type="submit" value="Go Back"/>
	</form>

  </body>
</html>