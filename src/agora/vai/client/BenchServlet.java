package agora.vai.client;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

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

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.backends.BackendService;
import com.google.appengine.api.backends.BackendServiceFactory;

import agora.vai.server.BenchThread;

@SuppressWarnings("serial")
public class BenchServlet extends HttpServlet{
	
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
		
		ArrayList<Thread> list = new ArrayList<Thread>();
		int numThread = 4;
		int count = 0;
		try {
			iter = upload.getItemIterator(request);
			
			while (iter.hasNext()) {
			    FileItemStream item = iter.next();
			    String name = item.getFieldName();
			    String nameFile;
			    InputStream stream = item.openStream();
			    nameFile = item.getName(); 
			    writer.println("File field " + name + " with file name "
			            + nameFile + " detected." + "<br>");
			    //writer.println(Streams.asString(stream));
			    byte[] bytes = IOUtils.toByteArray(stream);
			    //Benchmark b = new Benchmark("gaedistributedsystem.appspot.com",nameFile,bytes);
			    //b.doBenchmarck();
			    Thread thread = ThreadManager.createBackgroundThread(
			    		new BenchThread("gaedistributedsystem.appspot.com",nameFile,bytes));
				thread.start();
				list.add(thread);
				count++;
				if(count == numThread){
					for(Thread t : list){
						try {
							t.join();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					count = 0;
					list = new ArrayList<Thread>();
				}
			}
			if(count != 0){
				for(Thread t : list){
					try {
						t.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				count = 0;
				list = new ArrayList<Thread>();
			}
		} catch (FileUploadException e) {
			writer.println("No files out there!");
		} 
		writer.println("<br><br><br><form action=\"Teste.jsp\"><input type=\"submit\" value=\"Go Back\"/></form>");
		writer.println("</body>");
		writer.println("</html>");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		response.setHeader("Content-Type", "text/plain");
		BackendService backendsApi = BackendServiceFactory.getBackendService();
	    String backend = backendsApi.getBackendAddress("small");
	    if (backend != null) {
		      writer.println("Backend: " + backend + "\n");
		    }
	  }
}