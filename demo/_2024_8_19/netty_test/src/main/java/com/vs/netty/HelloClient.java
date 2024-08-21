package com.vs.netty;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
            // 创建客户端netty启动类
            // 创建group线程组处理连接建立，handler任务，channel负责连接的管理和
            Bootstrap clientBoot = new Bootstrap();
            clientBoot.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)       // 心跳机制
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new HelloClientHandler());
                        }
                    });
            // 配置完毕，启动客户端并阻塞等待连接建立，当任务由group处理完毕随机关闭channel管道和线程组group
            System.out.println("客户端已启动");
            ChannelFuture future = clientBoot.connect(host, port).sync();
            // 阻塞连接建立，并执行handler方法
            // ...

            // 注册连接关闭对象管理连接断开操作
            ChannelFuture closeFuture = future.channel().closeFuture();
            // 断开前进行的操作
            closeFuture.addListener((ChannelFutureListener) channelFuture -> System.out.println("我断开了"));
            // 阻塞等待异步线程的连接断开
            closeFuture.sync();
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

    // 业务处理
    // 连接建立执行的动作
    // 强烈不建议在初始化过程中进行IO操作，会阻塞线程的执行
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接已建立");
        // 需要单独开启一个独立线程处理异步IO操作，将主线程释放交由后续handler处理
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                System.out.println("请输入: ");
                String line = scanner.nextLine();
                if("quit".equals(line)) {
                    ctx.writeAndFlush(line);
                    // close异步关闭线程连接
                    ctx.close();
                    break;
                }
                ctx.writeAndFlush(line);
            }
        }).start();
    }

    // 连接断开时操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端已断开");
    }

    // 接收服务端返回的数据执行的动作
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 如果需要在读取服务器数据回写给服务器，就可以手动开辟bytebuf进行处理
//        ByteBuf pooledBuf = ctx.alloc().buffer();
        // bytebuf写writeandflush操作...
        System.out.println(msg);
//        response.release();   // 最后释放
    }

    // 异常处理
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
