package com.vs.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public class HelloServer {
     private final int port;

    public HelloServer(int port) {
        this.port = port;
    }

    public void start() {
        // 1. 定义父子线程监听连接情况
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 2. 初始化channel管道并添加业务逻辑任务
        try {
            // 创建服务器启动类并进行配置
            ServerBootstrap serverBoot = new ServerBootstrap();
            serverBoot.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 初始化数据管道(数据接收与回写的序列与反序列化，以及线程任务的注册)
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            System.out.println("连接已建立");
                            socketChannel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new HelloServerHandler());
                        }
                    });
            // 阻塞监听指定端口，并在连接建立后将任务交由handler异步处理
            System.out.println("服务已启动，正在监听: " + port);
            ChannelFuture future = serverBoot.bind(port).sync();
            // 阻塞等待连接断开并关闭channel管道和group线程池
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 如果连接出现异常则优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HelloServer(8080).start();
    }
}

// child任务处理器
class HelloServerHandler extends ChannelInboundHandlerAdapter {

    // 读取管道数据并打印或写回
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("正在读取客户端数据");
        System.out.println("接收到数据: " + msg);
        // 消息发回
        ctx.write(msg);
    }

    // 将数据从缓冲区发送并清空
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    // 出现异常时将连接关闭
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
