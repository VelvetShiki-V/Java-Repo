package com.vs.FileNet;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class FileClient {
    public static void main(String[] args) {
        // 1. 配置连接
        String serverAddress = "localhost";
        int port = 8888;
        String fileToSend = "D:\\Code\\java\\_2024_6_14\\assets\\rawContent";

        // 2. 与服务器建立连接，并开启流
        try (Socket socket = new Socket(serverAddress, port);
             // 读取文件内容
             FileInputStream fis = new FileInputStream(fileToSend);
             BufferedInputStream bis = new BufferedInputStream(fis);
             // 通过文件缓冲流与socket写给服务器
             BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {

            System.out.println("连接到服务器: " + serverAddress + " 端口: " + port);

            // 3. 读取文件并发送数据到服务器
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            // 4. 发送结束标记
            bos.flush(); // ******确保所有数据都被发送******
            socket.shutdownOutput();
            System.out.println("文件发送完成");



            // 5. 接收服务器的响应消息
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.println("服务器响应: " + reader.readLine());

                // 6. 清理资源
                socket.close();
                fis.close();
            }
        } catch (IOException e) {
            System.out.println("客户端发生错误: " + e.getMessage());
        }
    }
}
