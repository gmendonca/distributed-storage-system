package agora.vai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class Benchmark implements Runnable {
	private GcsService gcsService;
    private GcsFilename gcsFileName;
    private MemcacheService syncCache;
    private byte[] content;
    private String fileName;
    
    public Benchmark(String bucketName, String fileName, byte[] content) {
    	this.gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
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