package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,
            8,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());
    static ArrayList<Socket> list = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(10086);
        HashMap<String, String> hp = new HashMap<>();
        String s;
        BufferedReader br = new BufferedReader(new FileReader("save/userInfo.txt"));
        while ((s = br.readLine()) != null) {
            String username = s.split("&")[0].split("=")[1];
            String pwd = s.split("&")[1].split("=")[1];
            hp.put(username, pwd);
        }
        br.close();
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("有客户端来链接");
            //new Thread(new MyRun(socket, hp)).start();
            threadPoolExecutor.submit(new MyRun(socket, hp));
        }
    }
}
