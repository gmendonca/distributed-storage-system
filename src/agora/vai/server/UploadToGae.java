package agora.vai.server;

import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class UploadToGae {
	private GcsService gcsService;
    private String bucketName;
    private GcsFilename gcsFileName;

    public UploadToGae(String bucketName){
         this.gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
     	.initialRetryDelayMillis(10)
    	.retryMaxAttempts(10)
    	.totalRetryPeriodMillis(15000)
    	.build());
         this.bucketName = bucketName;
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

}