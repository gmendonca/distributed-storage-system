package agora.vai.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class BucketControl {
	private GcsService gcsService;
    private String bucketName;
    private GcsFilename gcsFileName;
    private MemcacheService syncCache;

    public BucketControl(String bucketName){
         this.gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
     	.initialRetryDelayMillis(10)
    	.retryMaxAttempts(10)
    	.totalRetryPeriodMillis(15000)
    	.build());
         this.bucketName = bucketName;
         this.syncCache = MemcacheServiceFactory.getMemcacheService();
    }

    public void writeToFile(String fileName, byte[] content) throws IOException {
         gcsFileName = new GcsFilename(bucketName, fileName);
         GcsOutputChannel outputChannel =
         gcsService.createOrReplace(gcsFileName, GcsFileOptions.getDefaultInstance());
         outputChannel.write(ByteBuffer.wrap(content));
         outputChannel.close();
    }

    public byte[] readFromFile(String fileName) throws IOException {
         gcsFileName = new GcsFilename(bucketName, fileName);
         int fileSize = (int) gcsService.getMetadata(gcsFileName).getLength();
         ByteBuffer result = ByteBuffer.allocate(fileSize);
         try (GcsInputChannel readChannel = gcsService.openReadChannel(gcsFileName, 0)) {
              readChannel.read(result);
         }
         return result.array();
    }
	
	public ListResult getListFiles(){
		GcsService gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
		AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();
	
		ListResult result;
		try {
			result = gcsService.list(appIdentity.getDefaultGcsBucketName(), ListOptions.DEFAULT);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Boolean removeFile(String fileName){
		gcsFileName = new GcsFilename(bucketName, fileName);
		Boolean result = false;
		try {
			result = gcsService.delete(gcsFileName);
			syncCache.delete(fileName);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Boolean checkFile(String fileName){
		ListResult list;
		ListItem item;;
	    String name;
		try {
			list = gcsService.list(bucketName, ListOptions.DEFAULT);
			while (list.hasNext()){
				item = list.next();
				name = item.getName();
				if(fileName.compareTo(name) == 0){
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean checkMemCache(String fileName){
		Object o = syncCache.get(fileName);
		return (o != null);
	}
	
	public void removeAllMemCache(){
		syncCache.clearAll();
	}
	
	public byte[] readFromCache(String fileName){
		Object o = syncCache.get(fileName);
		return (byte[])o;
	}
}
