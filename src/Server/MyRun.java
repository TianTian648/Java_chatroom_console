package Server;

import com.sun.source.tree.WhileLoopTree;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

public class MyRun implements Runnable {
    Socket socket;
    HashMap<String, String> hp;

    public MyRun(Socket socket, HashMap<String, String> hp) {
        this.socket = socket;
        this.hp = hp;
    }

    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String choose = br.readLine();
                switch (choose) {
                    case "login" -> login(br);
                    case "register" -> register(br);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void login(BufferedReader br) throws IOException {
        String s = br.readLine();
        String username = s.split("=")[0];
        String pwd = s.split("=")[1];
        if (hp.containsKey(username)) {
            if (hp.get(username).equals(pwd)) {
                writeback2client("登陆成功");
                Server.list.add(socket);
                talk2All(br, username);
            } else {
                writeback2client("用户名或密码错误");
            }
        } else {
            writeback2client("用户名不存在");
        }
    }

    private void register(BufferedReader br) throws IOException {
        String s = br.readLine();
        String username = s.split("=")[0];
        String pwd = s.split("=")[1];
        if (hp.containsKey(username)) {
            writeback2client("用户名重复");
        } else {
            hp.put(username, pwd);
            ArrayList<String> list = new ArrayList<>();
            hp.forEach((k, v) -> list.add("username=" + k + "&password=" + v));
            BufferedWriter bw = new BufferedWriter(new FileWriter("save/userInfo.txt"));
            for (String s1 : list) {
                bw.write(s1);
                bw.newLine();
            }
            bw.close();
            writeback2client("注册成功");
            Server.list.add(socket);
            talk2All(br, username);
        }
    }

    private void talk2All(BufferedReader br, String username) throws IOException {
        System.out.println(username + "发送了如下信息: ");
        String str1;
        while (true) {
            while ((str1 = br.readLine()) != null) {
                System.out.println(str1);
                for (Socket socket1 : Server.list) {
                    writeback2client(str1, socket1);
                }
            }


        }
    }

    private void writeback2client(String str) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(str);
        bw.newLine();
        bw.flush();
    }
    private void writeback2client(String str, Socket socket) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write(str);
        bw.newLine();
        bw.flush();
    }

}
