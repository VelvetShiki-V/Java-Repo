package com.vs.netty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
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
        // 1. 定义boss线程组管理连接创建，worker线程组管理channel套接字流入的数据流处理
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // 2. 初始化channel管道并为workerGroup添加业务逻辑任务
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
                            // 配置入站解码器、出站编码器和自定义的处理器
                            // pipeline确保handler正确执行顺序，每个handler可通过fireChannelRead传递给下一个handler
                            socketChannel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                            socketChannel.pipeline().addLast(new HelloServerHandler());
                            socketChannel.pipeline().addLast(new outBoundHandler());
                        }
                    });
            // 阻塞监听指定端口，并在连接建立后将任务交由handler异步处理
            ChannelFuture future = serverBoot.bind(port).sync();
            System.out.println("服务已启动，正在监听: " + port);
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

// child任务处理器(入站Inbound事件流)
// 线程安全
@ChannelHandler.Sharable
class HelloServerHandler extends ChannelInboundHandlerAdapter {
    // 连接断开时操作
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("与客户端连接已断开" + ctx.name());
    }

    // 读取管道数据并打印或写回
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("接收到数据: " + msg);
        // 创建一个池化缓冲区对象以开辟字节流对象存储数据
        ByteBuf response = ctx.alloc().buffer();
        try {
            response.writeBytes(("server received : " + msg).getBytes());
            // 消息发回，使用 ByteBuf.copy() 避免原始缓冲区被修改
            ctx.writeAndFlush(response.copy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 若后续没有其他handler需要使用原始缓冲区bytebuf，及时释放让引用计数归0
            response.release();
        }

        // 若有后续入站处理器，调用此方法将数据移交即可
//        super.channelRead(ctx, msg);
    }

    // 出现异常时将连接关闭
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}

// 出站事件流(仅有向channel写入数据才会触发)
class outBoundHandler extends ChannelOutboundHandlerAdapter {
    // write可以向客户端回写消息
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        System.out.println("我是出栈处理器函数write");
//        buf.release();
//        System.out.println("bytebuf已释放");
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        super.flush(ctx);
    }
}
