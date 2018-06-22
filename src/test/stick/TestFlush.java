package stick;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hl on 2018/6/22.
 */
public class TestFlush {

    public static void main(String[] args) throws IOException {
        File file = new File("/Users/hl/text1.txt");
        if(!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] b = new byte[1024];
        bos.write(b);
        //bos.flush();
    }
}
