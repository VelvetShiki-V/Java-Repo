package com.vs.Net;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    /*
        public static void main(String[] args) throws IOException {
            // 1. 创建服务器套接字
            ServerSocket ssoc = new ServerSocket(8888);
            System.out.println("监听8888端口中...");
            InputStream in = null;
            BufferedReader reader = null;

    //        while (true) {
            // 2. 监听指定端口，等待连接建立
            Socket soc = ssoc.accept();

            // 3. 连接建立，创建input流读取数据
            System.out.println(String.format("与客户端连接已建立, host: %s-%d", soc.getInetAddress().toString(), soc.getPort()));
            in = soc.getInputStream();
            // 用字符流与缓冲流加快读取效率
            reader = new BufferedReader(new InputStreamReader(in));
            String received_msg;
            // 打印流回写数据
            OutputStream out = soc.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            while ((received_msg = reader.readLine()) != null) {
                System.out.println("接收到客户端发送内容: " + received_msg);
                // 4. 创建output流回写数据
                writer.println(("服务端回写了: " + received_msg).getBytes());
    //            writer.close();
            }
    //        }
            // 5. 关闭处理
            reader.close();
            in.close();
            out.close();
        }
    */
    public static void main(String[] args) throws IOException {
        // 服务器与1234端口进行绑定
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server is listening on port 1234...");

        while (true) {
            // 连接建立，循环接收客户端数据
            try (Socket socket = serverSocket.accept()) {
                System.out.println(new String().format("连接已建立--ip: %s --port: %d", socket.getInetAddress(), socket.getPort()));

                // 建立高级流以读取和回写数据
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Received: " + inputLine);
                    out.println("Echo: " + inputLine);
                }

                // 客户端关闭来接，高级流读取为null（字节流read()返回-1，流关闭）
                System.out.println("连接已关闭");
                socket.shutdownOutput();
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port or listening for a connection");
                System.out.println(e.getMessage());
            }
        }
    }
}
