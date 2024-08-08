package com.vs.Net;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    /*
        public static void main(String[] args) throws IOException {
            // 1. 创建客户端套接字建立与服务器连接
            String addr = "localhost";
            int host = 8888;
            Socket soc = new Socket(addr, host);
            System.out.println("与服务端连接已建立, 端口: " + host);

            // 2. 创建读写流，用于发送数据流与接收流数据
            OutputStream out = soc.getOutputStream();
            InputStream in = soc.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            // 3. 写数据和读数据
            Scanner sc = new Scanner(System.in);
    //        while (true) {
            System.out.println("请输入内容: ");
            String msg = sc.nextLine();
    //            if (msg == "quit") break;
    //            else {
            out.write(msg.getBytes());
            System.out.println("客户端内容已发送: " + msg);
            String writeBackMsg = "";
            while ((writeBackMsg = reader.readLine()) != null) {
                System.out.println("接收到服务端回写： " + writeBackMsg);
            }
            // 4. 关闭处理
            reader.close();
            in.close();
            out.close();
        }
    */
//        }
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int port = 1234;

        // 创建客户端套接字与服务器建立连接
        try (Socket socket = new Socket(serverAddress, port);
             // 标准输入流
             Scanner sc = new Scanner(System.in);
             // 字节流转打印流向服务器发送数据
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             // 字节流转字符流转缓冲流接收服务器响应数据
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("连接已建立");
            // 发送数据
            while (true) {
                System.out.println("请输入内容: ");
                String msg = sc.nextLine();
                out.println(msg);
                // 判退出条件不能用==，用equals
                if("quit".equals(msg)) break;
                // 接收数据
                System.out.println(in.readLine());
//                String response;
//                while ((response = in.readLine()) != null) {
//                    System.out.println("Server says: " + response);
//                }
            }
            socket.shutdownOutput();    // 结束标记
            // 或
//            out.close();
//            in.close();
        }
    }
}

