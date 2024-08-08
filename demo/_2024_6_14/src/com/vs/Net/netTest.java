package com.vs.Net;
import java.io.IOException;
import java.net.*;

public class netTest {
/*    public static void main(String[] args) {
        netTest n = new netTest();
        n.test_send();
    }*/

    public void test_send() {
        // 获取本机IP地址和主机名
/*        try {
            InetAddress addr = InetAddress.getByName("VelvetShiki");
            System.out.println(addr);
            System.out.println(addr.getHostAddress());
            System.out.println(addr.getHostName());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }*/

        // 发送数据UDP
        try {
            // 1. 创建套接字准备发送数据
            DatagramSocket soc = new DatagramSocket();
            // 2. 装填数据并打包
            byte[] msg = new String("this is a message needs to be delivered").getBytes();
            System.out.println(new String(msg));
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            // 参数：数据，数据长度，ip, host
            DatagramPacket packet = new DatagramPacket(msg, msg.length, addr, 8080);
            // 3. 发送数据
            soc.send(packet);
            // 4. 关闭套接字
            soc.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test_receive() {

        // 接收数据
        // 1. 创建需要监听的套接字
        DatagramSocket rec_soc = null;
        try {
            rec_soc = new DatagramSocket(8080);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        // 2. 创建接收的字节数组缓冲区
        byte[] buffer = new byte[1024];
        // 2. 创建UDP数据报整体接收
        DatagramPacket rec_packet = new DatagramPacket(buffer, buffer.length);
        try {
            rec_soc.receive(rec_packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 4. 观察接收的内容
        System.out.println("接收到数据");
        System.out.println(rec_packet.getAddress());
        System.out.println(rec_packet.getPort());
        System.out.println(new String(rec_packet.getData()));
        rec_soc.close();
    }
}




