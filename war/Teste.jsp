<!doctype html>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<html>
  <head>
    <title>Distributed Storage System</title>
  </head> 
  <body>

    <h1>Distributed Storage System</h1>
	<form action="UploadServlet" method="post" enctype="multipart/form-data">
    	<input type="file" name="file" multiple/>
    	<input type="submit" value="Upload"/>
	</form>
	<br><br><br>
	<form action="ShowFileList.jsp" >
    	<input type="submit" value="Show Files"/>
	</form>
	<br><br><br>
	<form action="RemoveFile.jsp" >
		<input type="text" name="description" />
    	<input type="submit" value="Remove File"/>
	</form>
	<br><br><br>
	<form action="CheckFile.jsp" >
		<input type="text" name="description" />
    	<input type="submit" value="Check File"/>
	</form>
	<br><br><br>
	<form action="ReadFile.jsp" >
		<input type="text" name="description" />
    	<input type="submit" value="Read File"/>
	</form>
	<br><br><br>
	<form action="<%= blobstoreService.createUploadUrl("/BenchServletBlob") %>" method="post" enctype="multipart/form-data">
    	Num of Threads :<input type="text" name="threads" />
    	<input type="file" name="file" multiple/>
    	<input type="submit" value="Benchmark"/>
	</form>
  </body>
</html>