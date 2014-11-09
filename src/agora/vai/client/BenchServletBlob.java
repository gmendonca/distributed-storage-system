package agora.vai.client;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

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
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import agora.vai.server.BenchThread;

@SuppressWarnings("serial")
public class BenchServletBlob extends HttpServlet{
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer = response.getWriter();
		//ServletFileUpload upload = new ServletFileUpload();
		//response.setContentType("text/plain");
		response.setContentType("text/html");
		Iterator iter;
		writer.println("<!DOCTYPE html>");
		writer.println("<html>");
		writer.println("<head>");
		writer.println("<title>Distributed Storage System</title>");
		writer.println("<body>");
		writer.println("<h1>Distributed Storage System</h1>");
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

		
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(request);
		
		ArrayList<Thread> list = new ArrayList<Thread>();
		int numThread = 4;
		int count = 0;
		
			//iter = upload.getItemIterator(request);
			
			iter = blobs.entrySet().iterator();
			
			while (iter.hasNext()) {
				Map.Entry pairs = (Map.Entry)iter.next();
				String nameFile = (String) pairs.getKey();
				BlobKey blobKey = (BlobKey) pairs.getValue();
				BlobstoreInputStream in = new BlobstoreInputStream(blobKey); 
				
				//byte[] b = blobInfo.getContentType().getBytes(Charset.forName("UTF-8"));
				byte[] b= IOUtils.toByteArray(in);
			    Thread thread = ThreadManager.createBackgroundThread(
			    		new BenchThread("gaedistributedsystem.appspot.com",nameFile,b));
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
		
		writer.println("<br><br><br><form action=\"Teste.jsp\"><input type=\"submit\" value=\"Go Back\"/></form>");
		writer.println("</body>");
		writer.println("</html>");
	}
}