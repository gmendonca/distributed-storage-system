<!doctype html>
<%@ page import="agora.vai.server.BucketControl"%>

<%
BucketControl bc = new BucketControl("gaedistributedsystem.appspot.com");
String fileName = "teste";
Boolean cache = false;
Boolean bucket = false;
if(request.getParameter("description") != null){
	fileName = request.getParameter("description");
	cache = bc.checkMemCache(fileName);
	if(!cache) bucket = bc.checkFile(fileName);
}
%>

<html>
  <head>
    <title>Distributed Storage System</title>
  </head>  
  <body>

    <h1>Distributed Storage System</h1>
    
<%
	if(cache) {
%>
	<p><%= fileName %> Founded in cache!</p>
<%
	}
%>

<%
	if(bucket) {
%>
	<p><%= fileName %> Founded in bucket!</p>
<%
	}
%>

	<br><br><br>
	<form action="Teste.jsp" >
    	<input type="submit" value="Go Back"/>
	</form>

  </body>
</html>