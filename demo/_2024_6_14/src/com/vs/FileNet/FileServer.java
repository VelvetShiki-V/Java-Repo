package com.vs.FileNet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.*;

public class FileServer {
    public static void main(String[] args) {
        // 连接配置
        int port = 8888;

        // 监听等待连接
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("服务器启动，正在监听端口 " + port);

            while (true) {
                // 连接建立，创建线程提供服务
                Socket clientSocket = serverSocket.accept();

                // 多线程版服务器
//                new Thread(new myTask((clientSocket))).start();

                // 线程池版服务器（工厂线程池）
//                ExecutorService pool = Executors.newCachedThreadPool();
                // 自定义线程池
                ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 6, 60, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
                pool.submit(new myTask(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("服务器启动失败: " + e.getMessage());
        }
    }
}

// 多线程优化服务器
class myTask implements Runnable {

    private Socket clientSocket;

    public myTask (Socket soc) {
        this.clientSocket = soc;
    }

    @Override
    public void run() {
        String filePath = "D:\\Code\\java\\_2024_6_14\\output\\" + UUID.randomUUID().toString().replace("-", "");
        try (
                // 缓冲流接收客户端数据
                BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());
                // 文件流与缓冲流写到新文件
                // UUID生成随机文件名
                FileOutputStream fos = new FileOutputStream(filePath);
                BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            System.out.println("已接收到客户端连接");

            byte[] buffer = new byte[1024];
            int bytesRead;
            // 读取客户端发送的数据
            while ((bytesRead = bis.read(buffer)) != -1) {
//                        System.out.println(new String(buffer));
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush(); // ******确保所有数据都被写入文件******

            System.out.println("文件接收完成，保存路径: " + filePath);

            // 发送确认消息给客户端
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("文件接收并保存成功。");
        } catch (IOException e) {
            System.out.println("处理客户端连接时发生错误: " + e.getMessage());
        }
    }
}
