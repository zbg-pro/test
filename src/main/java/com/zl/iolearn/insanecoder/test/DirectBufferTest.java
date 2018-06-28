package com.zl.iolearn.insanecoder.test;

import org.junit.Test;
import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Created by hl on 2018/6/26.
 */
public class DirectBufferTest {

    public static void clean(final ByteBuffer byteBuffer){
        if(byteBuffer.isDirect())
            ((DirectBuffer)byteBuffer).cleaner().clean();
    }

    public static void sleep(long i) {
        try {
            Thread.sleep(i);
        }catch(Exception e) {
          /*skip*/
        }
    }

    @Test
    public void testDirectBuffer(){
        //ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
        System.out.println("start");
        //sleep(1000);
        buffer.put("FC in world".getBytes());
        //byte[] bb = new byte[100];
        //buffer.get(b);

        assert 1==1;
        System.out.println(buffer.limit());
        System.out.println(buffer.capacity());
        //循环输出每个字节内容
        for (int i = 0; i < buffer.capacity(); i++) {
            byte b = buffer.get(i); //读操作，指针会自动移动
            if(i>100)
                break;
            System.out.println((char)b);
        }

        clean(buffer);
        System.out.println("end");
        sleep(2000);
    }

}
