package stick.tcp2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hl on 2018/6/26.
 */
public class HeavyTCPClient {

    private static ExecutorService tp = Executors.newCachedThreadPool();

    private static final int sleep_time = 1000*1000*1000;

    static CountDownLatch downLatch = new CountDownLatch(10);

    public static class EchoClient implements Runnable{


        @Override
        public void run() {
            Socket client = null;
            PrintWriter pw = null;
            BufferedReader br = null;
            try {
                client = new Socket("127.0.0.1", 1122);
                pw = new PrintWriter(client.getOutputStream(), true);
                pw.print("'type':0,'dbopt':0,'data':{ \"client_ip\": " +
                        "\"192.168.102.29\", \"server_ip\": \"74.125.204.113\", \"client_port\": 13698 }}\r\n" +
                        "{'probeid':0,'type':0,'dbopt':0,'data':{ \"client_ip\": \"192.168.102.29\", \"server_ip\": \"74.125.204.113\", " +
                        "\"client_port\": 13698 }}\r\n{'probeid':0,'type':0");

                pw.flush();


                br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                System.out.println("from server: " + br.readLine());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pw != null)
                        pw.close();
                    if (br != null)
                        br.close();
                    if (client != null)
                        client.close();
                    downLatch.countDown();
                } catch (IOException e){}
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            EchoClient client = new EchoClient();
            tp.submit(client);
        }

        downLatch.await();
        tp.shutdown();
    }


}
