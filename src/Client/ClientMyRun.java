package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientMyRun implements Runnable{
    Socket socket;

    public ClientMyRun(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        //线程处理信息接收
        while (true) {
            try {
                //接收服务器发送过来的聊天记录
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String msg = br.readLine();
                System.out.println(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
