package agora.vai.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.appengine.api.ThreadManager;

public class Benchmark {
    private byte[] content;
    private String fileName;
    private String bucketName;
    
    public Benchmark(String bucketName, String fileName, byte[] content) {
        this.content = content;
        this.fileName = fileName;
        this.bucketName = bucketName;
   }
   
   public void doBenchmarck(){
	   Thread thread = ThreadManager.createBackgroundThread(new BenchThread(bucketName, fileName, content));
	   thread.start();
	   
   }
   

}