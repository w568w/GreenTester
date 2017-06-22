package ml.qingsu.greenrunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Trumeet on 2017/6/20.
 * @author Trumeet
 */

public class FileUtils {
    /**
     * Write byte array to file
     * @param data Byte array data
     * @param filename name
     * @param directory File directory
     * @throws IOException
     */
    public static void writeFile (byte[] data,
                                  String filename, File directory) throws IOException {
        File f = new File(directory.getPath()+ File.pathSeparator +filename);
        if(f.exists())
            f.delete();
        if(!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream fops = new FileOutputStream(f);
        fops.write(data);
        fops.flush();
        fops.close();
    }
}
