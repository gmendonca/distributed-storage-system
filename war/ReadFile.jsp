<!doctype html>
<%@ page import="agora.vai.server.BucketControl"%>

<%
BucketControl bc = new BucketControl("gaedistributedsystem.appspot.com");
String fileName = "teste";
byte[] bytes = new byte[1024];
Boolean cache = false;
if(request.getParameter("description") != null){
	fileName = request.getParameter("description");
	cache = bc.checkMemCache(fileName);
	if(cache){
		bytes = bc.readFromCache(fileName);
	}
	else {
		bytes = bc.readFromFile(fileName);
	}
}
%>

<html>
  <head>
    <title>Distributed Storage System</title>
  </head>
  <body>
  	<h1>Distributed Storage System</h1>
  	<br><br>
	<form action="Teste.jsp" >
    	<input type="submit" value="Go Back"/>
	</form>

<%
	if(cache) {
%>
	<p> Read from cache: </p>
<%
	}
%>  
<%
	String str = new String(bytes, "UTF-8");
%>
	<p><%= str %></p>


	

  </body>
</html>