package stick.tcp;

import java.io.*;
import java.net.Socket;

/**
 * Created by hl on 2018/6/25.
 */
public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            //1 创建指定ip端口的客户端
            socket = new Socket("localhost", 1122);

            //2 获取输出通道，给通道中发送消息
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
//            pw.write("用户名：whf;密码：789");
//            pw.flush();

            for (int i = 0; i < 1000000; i++) {
                pw.write("用户名：whf;密码：789");
//                if(i%200==0)
//                    pw.write("\n");
            }
            pw.flush();

            socket.shutdownOutput();


            //3 获取输入流， 从通道中读取消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String info = null;
            while ((info = br.readLine()) == null) {
                System.out.println("client receive server info: " + info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket != null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
