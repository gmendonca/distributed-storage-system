import java.io.*;
import java.util.*;

public class GenerateFiles {

  static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static Random rnd = new Random();

  private String randomString(int len) {
     StringBuilder sb = new StringBuilder( len );
     for( int i = 0; i < len; i++ )
        sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
     return sb.toString();
  }

  public static void main(String[] args){

    GenerateFiles g = new GenerateFiles();

    String fileName;

    int howMuch = Integer.parseInt(args[0]);
    int howMany = Integer.parseInt(args[1]);

    try{
      for(int m = 0; m < howMany; m++){
        fileName = "dataset/" + g.randomString(10);
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        for(int i = 0; i < howMuch; i++){
          for(int j = 0; j < 10; j++){
            writer.println(g.randomString(100));
          }
          writer.println(g.randomString(24));
        }
        writer.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
