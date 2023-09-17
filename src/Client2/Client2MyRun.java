package Client2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client2MyRun implements Runnable{
    Socket socket;

    public Client2MyRun(Socket socket) {
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
