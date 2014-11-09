package agora.vai.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
//import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import agora.vai.server.UploadToGae;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		ServletFileUpload upload = new ServletFileUpload();
		//response.setContentType("text/plain");
		response.setContentType("text/html");
		FileItemIterator iter;
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>Distributed Storage System</title>");
		writer.println("<body>");
		writer.println("<h1>Distributed Storage System</h1>");
		try {
			iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    String nameFile;
			    InputStream stream = item.openStream();
			    nameFile = item.getName(); 
			    writer.println("File with file name "
			            + nameFile + " uploaded." + "<br>");
			    //writer.println(Streams.asString(stream));
			    byte[] bytes = IOUtils.toByteArray(stream);
			    UploadToGae ugae = new UploadToGae("gae-distributed.appspot.com");
                ugae.writeToFile(nameFile, bytes);
                ugae.setCache(nameFile, bytes);
			}
		} catch (FileUploadException e) {
			writer.println("No files out there!");
		}
		writer.println("<br><br><br><form action=\"Teste.jsp\"><input type=\"submit\" value=\"Go Back\"/></form>");
		writer.println("</body>");
		writer.println("</html>");
	}
}
