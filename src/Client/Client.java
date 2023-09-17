package Client;

import Server.MyRun;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 10086);
        System.out.println("服务器已经连接成功");
        while (true) {

            System.out.println("==============欢迎来到聊天室================");
            System.out.println("1登录\n" +
                    "2注册\n" +
                    "请输入您的选择：");
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            switch (s) {
                case "1" -> login(socket);
                case "2" -> register(socket);
                default -> System.out.println("没有这个选项");
            }
        }

    }

    private static void login(Socket socket) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.nextLine();
        System.out.println("请输入密码");
        String pwd = sc.nextLine();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("login");
        bw.newLine();
        bw.flush();

        bw.write(username + "=" + pwd);
        bw.newLine();
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = br.readLine();
        if ("登陆成功".equals(s)) {
            System.out.println("登陆成功，开始聊天");
            talk2Server(socket);
        } else if ("用户名或密码错误".equals(s)) {
            System.out.println("用户名或密码错误");
        } else {
            System.out.println("用户名不存在");
        }
    }

    private static void talk2Server(Socket socket) throws IOException {
        new Thread(new ClientMyRun(socket)).start();
        talk2ALl(socket);
    }

    private static void register(Socket socket) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入用户名");
        String username = sc.nextLine();
        System.out.println("请输入密码");
        String pwd = sc.nextLine();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bw.write("register");
        bw.newLine();
        bw.flush();

        bw.write(username + "=" + pwd);
        bw.newLine();
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s = br.readLine();

        if ("用户名重复".equals(s)) {
            System.out.println("用户名重复");
        } else if ("注册成功".equals(s)) {
            System.out.println("注册成功");
            talk2Server(socket);
        }
    }

    private static void talk2ALl(Socket socket) throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要说的话");
            String s = sc.nextLine();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write(s);
            bw.newLine();
            bw.flush();
        }
    }
}
