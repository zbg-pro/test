package stick;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hl on 2018/6/22.
 */
public class TCPServer {

    public TCPServer(){}

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(6666);
        while(true){
            Socket s = ss.accept();//如果当前没有连接，会阻塞在这里不继续执行
            DataInputStream dis = new DataInputStream(s.getInputStream());//获取客户端发送过来的消息
            System.out.println("server："+ dis.readUTF());

            DataOutputStream dos =new DataOutputStream(s.getOutputStream());//给客户端写入消息
            Thread.sleep(2000);
            dos.writeUTF("client's ip & port:"+ s.getInetAddress() +"__" + s.getPort());//这边是阻塞的，client 没有写 没有flush，这边会一直会block。 这个时候 其他线程进不来
            System.out.println("write done");

            dos.flush();
            dos.close();// 注意 无论是dos 还是 dis invoke close方法，都会将当前方法栈 socket 关闭 ，不过while里又new 出来个新的
        }

    }
}
