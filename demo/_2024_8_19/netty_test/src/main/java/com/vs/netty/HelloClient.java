package com.vs.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;

public class HelloClient {
    private final String host;
    private final int port;

    public HelloClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 创建客户端netty启动类，并创建group线程组处理handler任务，channel负责数据序列化
            Bootstrap clientBoot = new Bootstrap();
            clientBoot.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)       // 心跳机制
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new HelloClientHandler());
                        }
                    });
            // 配置完毕，启动客户端并阻塞等待连接建立，当任务由group处理完毕随机关闭channel管道和线程组group
            System.out.println("客户端已启动");
            ChannelFuture future = clientBoot.connect(host, port).sync();
            // 阻塞等待直接连接断开
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HelloClient("localhost", 8080).start();
    }
}

class HelloClientHandler extends ChannelInboundHandlerAdapter {

    // 连接建立执行的动作
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接已建立");
        ctx.writeAndFlush("你好，我是客户端");
    }

    // 接收服务端返回的数据执行的动作
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("server says: " + msg);
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
