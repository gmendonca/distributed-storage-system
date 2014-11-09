<!doctype html>
<%@ page import="agora.vai.server.BucketControl"%>
<%@ page import="com.google.appengine.tools.cloudstorage.ListResult"%>
<%@ page import="com.google.appengine.tools.cloudstorage.ListItem"%>

<%
BucketControl bc = new BucketControl("gaedistributedsystem.appspot.com");
ListResult list = bc.getListFiles();
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
	<br><br>

<%
	while (list.hasNext()){
    ListItem l = list.next();
    String name = l.getName();
%>
	<p><%= name %></p>
<%
	}
%>

  </body>
</html>