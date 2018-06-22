package stick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

/**
 * Created by hl on 2018/6/22.
 */
public class TCPClient {

    public TCPClient() {
    }

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("127.0.0.1", 6666);// sever connect 建立服务器连接
        System.out.println("a client connect from client！");

        //debug
        DataOutputStream dos =new DataOutputStream(s.getOutputStream());//写数据给服务器
        dos.writeUTF("i am client");
        dos.flush();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        System.out.println(dis.readUTF());// 这边是阻塞的，client 没有写
        // 没有flush，这边会一直会block。 这个时候 其他线程进不来
        dis.close();
        s.close();
    }
}
