package agora.vai.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class BenchThread implements Runnable{
		private GcsService gcsService;
	    private GcsFilename gcsFileName;
	    private MemcacheService syncCache;
	    private byte[] content;
	    private String fileName;
	    
	    public BenchThread(String bucketName, String fileName, byte[] content) {
	    	this.gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
	     	.initialRetryDelayMillis(10)
	    	.retryMaxAttempts(10)
	    	.totalRetryPeriodMillis(1500000)
	    	.build());
	        this.gcsFileName = new GcsFilename(bucketName, fileName);
	        this.content = content;
	        this.fileName = fileName;
	   }
	   
	   public void writeToFile() throws IOException {
	        GcsOutputChannel outputChannel =
	        gcsService.createOrReplace(gcsFileName, GcsFileOptions.getDefaultInstance());
	        outputChannel.write(ByteBuffer.wrap(content));
	        outputChannel.close();
	   }
	   
	   public void setCache(){
	   	if(content.length > 100*1024)
	   		return;
			syncCache.put(fileName, content, Expiration.byDeltaSeconds(86400));
		}
	    
	    public void run() {
	    	try {
				writeToFile();
				setCache();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	}