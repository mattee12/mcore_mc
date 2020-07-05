package net.mattee.mcore.Files;

import net.mattee.mcore.main.mCore;

import java.io.*;

public class Files {

    public static Files FILES;

    public Files(){
        FILES = this;
    }

    /*
    Copies the given file from the compiled JAR file to the given external path.
     */
    public void copyResource(String path, File output){
        try {
            output.createNewFile();
            InputStream in = mCore.PLUGIN.getResource(path);
            OutputStream out = new FileOutputStream(output);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
