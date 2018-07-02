package com.zl.iolearn.insanecoder.test;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by hl on 2018/5/8.
 */
public class ByteBufferTest {

    public static void main(String[] args) {

        //10个字节大小
        ByteBuffer buffer = ByteBuffer.allocate(10);

        //容量是10，EOF位置是10，初始位置也是0
        v(buffer.capacity());
        v(buffer.limit());

        v("*********1*********");

        //输出看看，输出是10个0
        printBuffer(buffer);
        v("*********2*********");
        //此时，指针指向位置10，已经是最大容量了。
        //把指针挪回位置1
        v(buffer.position());
        buffer.rewind();
        v(buffer.position());
        //写操作，指针会自动移动
        buffer.putChar('a');
        v(buffer.position()); //指针指向2
        buffer.putChar('啊');
        v(buffer.position()); //指针指向4
        v("*********3*********");
        //当前位置设置为EOF，指针挪回位置1
        //相当于下面两句：
        //buffer.limit(4);
        //buffer.position(0);
        buffer.flip();

        //输出前4个字节看看，输出是0 61 55 4a
        printBuffer(buffer);
        v("******************");
        //指针挪到位置1，压缩一下
        //输出是61 55 4a 4a 0 0 0 0 0 0
        //compact方法会把EOF位置重置为最大容量，这里就是10
        buffer.position(1);
        buffer.compact();
        printBuffer(buffer);
        v("******************");
        //注意当前指针指向3，继续写入数据的话，就会覆盖后面的数据了。
        v(buffer.position());

        buffer.clear();//

        buffer.limit();//

        buffer.compact();//

        buffer.remaining();//

        buffer.arrayOffset();

        buffer.asShortBuffer();//

        buffer.duplicate();//

        buffer.order();//

        buffer.reset();//

        buffer.isReadOnly();//

        buffer.slice();//

        buffer.put(new byte[1]);//

        buffer.isDirect();//

        buffer.limit(1);//

        buffer.mark();//

        buffer.flip();
        v("******************");
    }

    /**
     * 输出buffer内容.
     */
    public static void printBuffer(ByteBuffer buffer) {

        //记住当前位置
        int p = buffer.position();

        //指针挪到0
        buffer.position(0);

        //循环输出每个字节内容
        for (int i = 0; i < buffer.limit(); i++) {
            byte b = buffer.get(); //读操作，指针会自动移动
            v(Integer.toHexString(b));
        }

        //指针再挪回去
        buffer.position(p);

        //本想用mark()和reset()来实现。
        //但是，它们貌似只能正向使用。
        //如，位置6的时候，做一下Mark，
        //然后在位置10（位置要大于6）的时候，用reset就会跳回位置6.

        //而position(n)这个方法，如果之前做了Mark，但是Mark位置大于新位置，Mark会被清除。
        //也就是说，做了Mark后，只能向前跳，不能往回跳，否则Mark就丢失。
        //rewind()方法，更干脆，直接清除mark。
        //flip()方法，也清除mark
        //clear()方法，也清除mark
        //compact方法，也清除mark

        //所以，mark方法干脆不要用了，自己拿变量记一下就完了。
    }

    public static void v(Object o, boolean isline) {
        if(isline)
            System.out.println(o);
        else
            System.out.print(o);
    }

    public static void v(Object o) {
      System.out.println(o);
    }

    ByteBuffer buffer = ByteBuffer.allocate(100);

    // 字符集处理类
    private Charset charset = Charset.forName("utf8");

    @Test
    public void testCapPosLimt(){
        v(buffer.capacity());
        v(buffer.position());
        v(buffer.limit());
        v("-------");
        byte b = 1;
        buffer.put(b);//+1
        buffer.putChar('a');//+2
        buffer.putShort((short)1);//+2
        buffer.putInt(1);//+4
        buffer.putFloat(2.1f);//+4
        buffer.putLong(1l);//+8
        buffer.putDouble(2.1d);//+8

        buffer.putDouble(2.2d);//+8
        //v(buffer.getDouble());//+8

        //v(buffer.limit(1));buffer.putDouble(1, 2.2d);// 读写限制：也就是缓冲区可以利用（进行读写）的范围的最大值  0=<position=<limit=<capacity
        //buffer.flip();//

        buffer.put("abcd123".getBytes());//+7  44

        v(buffer.limit());
        v(buffer.position());
        v(buffer.remaining());

        //buffer.rewind();==buffer.position(0)

        /*buffer.flip();//把limit位置设置到position
        buffer.limit(buffer.limit());//设置limit的大小
        buffer.position(0);*/


        /*
        ByteBuffer buf = buffer.slice();

        byte[] bb = new byte[buffer.limit()];
        for (int i = 0; i < buf.limit(); i++) {
            bb[i] = buf.get(i);
        }
        //错误代码 System.out.println(new String(buf.array()));
        System.out.println(new String(bb));
        String receiveData = charset.decode(buffer).toString();
        System.out.println(receiveData);*/
    }

    @Test
    public void testRewind() {
        testCapPosLimt();
        v(buffer.rewind());//重新开始position

        v("-----");

        v(buffer.capacity());
        v(buffer.position());
        v(buffer.limit());
    }

    /**
     * 类似书签，当读取到,写入到某个位置时候，mark一下。然后后续再继续操作读取或者写入后，如果reset一下，发现又回到了mark点
     */
    @Test
    public void testMark(){
        testCapPosLimt();
        v(buffer.mark());
        v(buffer.position());
        v(buffer.limit());

        v("--1--");
        v("sss "+buffer.getInt());
        buffer.putInt(22);

        v(buffer.position());
        buffer.reset();
        v(buffer.position());
        v(buffer.limit());

    }

    @Test
    public void testRemaining(){
        testCapPosLimt();
        v(buffer.remaining());
    }

    /**
     * 让remain作为真正有内容的长度，position
     */
    @Test
    public void testCompact(){
        testCapPosLimt();
        buffer.flip();//单纯执行flip，让limit真正在position位置（这个位置共同由读写过程计算出），position位置归零，remain位置为计算后的limit位置
        //buffer.compact();
        v("-----");
        v(buffer.limit());
        v(buffer.position());
        v(buffer.remaining());

    }

    @Test
    public void testSlice() {
        buffer = ByteBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }

        buffer.position(3);
        buffer.limit(7);
        ByteBuffer slice = buffer.slice();
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 11;
            slice.put(i, b);
        }

        buffer.position( 0 );
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        //子缓存区相当于buffer缓存区的一个窗口，窗口的start和end是指定的位置
    }

    @Test
    public void testOrder1(){
        //buf.put("abcde".getBytes());
        CharBuffer charBuffer = buffer.asCharBuffer().put("abcde");

        v(Arrays.toString(buffer.array()));
        v("----------------------------------------------");

        buffer.rewind();
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asCharBuffer().put("abcde");
        v(Arrays.toString(buffer.array()));
        v("----------------------------------------------");


        buffer.rewind();
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.asCharBuffer().put("abcde");
        v(Arrays.toString(buffer.array()));/**/
    }

    /**
     * 前者把每个字符转为byte，遍历到char转byte个byte
     */
    @Test
    public void testSlice2() throws UnsupportedEncodingException {
        buffer = ByteBuffer.allocate(20);;
        buffer.put("abcde张".getBytes());
        buffer.put((byte) 'f');
        v(Arrays.toString(buffer.array()));

        String s = new String();

        v(new String(buffer.array()));

        buffer.position( 0 );
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.print(buffer.get()+ ",");
        }
        v("\n");

        buffer.position(5);
        buffer.limit(8);

        v(buffer.position());
        v(buffer.limit());
        v(buffer.capacity());

        ByteBuffer slice = buffer.slice();
        v(slice.capacity());

        byte[] hanzi = new byte[3];
        for (int i = 0; i < slice.capacity(); i++) {
            hanzi[i] = slice.get(i);
        }

        v(Arrays.toString(hanzi));
        v(new String(hanzi, "UTF-8"));
    }

    @Test
    public void testOrder2(){
        ByteBuffer buf = ByteBuffer.wrap(new byte[10]);
        buf.asCharBuffer().put("abcde");
        System.out.println(Arrays.toString(buf.array()));
        System.out.println("----------------------------------------------");

        buf.rewind();
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.asCharBuffer().put("abcde");
        System.out.println(Arrays.toString(buf.array()));
        System.out.println("----------------------------------------------");

        buf.rewind();
        buf.order(ByteOrder.LITTLE_ENDIAN);
        //buf.asCharBuffer().put("abcde");
        System.out.println(Arrays.toString(buf.array()));
    }

    @Test
    public void testDirect(){
        buffer = ByteBuffer.allocateDirect(10);
        v(buffer.isDirect());
        buffer.position(10);
        v(buffer.isReadOnly());
        buffer.duplicate();
        /**
         * duplicate()方法用于创建一个与原始缓冲区共享内容的新缓冲区。新缓冲区的position，
         * limit，mark和capacity都初始化为原始缓冲区的索引值，然而，它们的这些值是相互独立的。
         */
    }

    @Test
    public void testArrayoffset(){//todo ?
        buffer = ByteBuffer.wrap(new byte[20], 2, 12);
        buffer.asCharBuffer().put("abcedf");
        v(buffer.arrayOffset());
        v(Arrays.toString(buffer.array()));
        v(buffer.hasArray());
        /**
         * 新的缓冲区将由给定的 byte 数组支持；也就是说，缓冲区修改将导致数组修改，反之亦然。新缓冲区的容量将为array.length，其位置将为offset，
         * 其界限将为 offset + length，其标记是不确定的。其底层实现数组将为给定数组，并且其数组偏移量将为零。
         */
    }

    @Test
    public void testByteBufferHeader(){
        buffer.put("3abc5efghijkl".getBytes());
        buffer.flip();

        //get如果不加参数，则读取当前position位置的数据
        v(buffer.position());
        int length = (char)buffer.get(0) - '0';
        v(length);
    }

}
