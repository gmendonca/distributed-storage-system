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
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import agora.vai.server.UploadToGae;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		ServletFileUpload upload = new ServletFileUpload();
		response.setContentType("text/plain"); 
		FileItemIterator iter;
		try {
			iter = upload.getItemIterator(request);
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    InputStream stream = item.openStream();
			    if (item.isFormField()) {
			        writer.println("Form field " + name + " with value "
			            + Streams.asString(stream) + " detected.");
			    } else {
			    	writer.println("File field " + name + " with file name "
			            + item.getName() + " detected.");
			    	//writer.println(Streams.asString(stream));
			    	byte[] bytes = IOUtils.toByteArray(stream);
			    	UploadToGae ugae = new UploadToGae("gaedistributedsystem.appspot.com");
                    ugae.writeToFile("teste2.txt", bytes);
			    }
			}
		} catch (FileUploadException e) {
			writer.println("No files out there!");
		}
	}
}
