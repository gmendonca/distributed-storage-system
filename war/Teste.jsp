<!doctype html>

<html>
  <head>
    <title>Distributed Storage System</title>
    
  <body>

    <h1>Distributed Storage System</h1>
	<form action="UploadServlet" method="post" enctype="multipart/form-data">
    	<input type="file" name="file" />
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
  </body>
</html>