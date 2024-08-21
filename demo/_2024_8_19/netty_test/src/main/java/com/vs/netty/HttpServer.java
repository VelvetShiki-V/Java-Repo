package com.vs.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

public class HttpServer {
    private final int port;

    public HttpServer (int port) {
        this.port = port;
    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            System.out.println("链接已建立" + socketChannel.remoteAddress());
                            // 向通道的管道中添加 HttpServerCodec，用于 HTTP 请求的解码和编码
                            // codec即是入站处理器，也是出站处理器
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            // HttpObjectAggregator 用于将多个消息整合为一个 FullHttpRequest 或 FullHttpResponse
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                            // 业务处理器
                            // 监听特定类型请求
                            socketChannel.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
                                    System.out.println("获取到请求行: " + fullHttpRequest.uri());

                                    // 处理客户端发来的 HTTP 请求
                                    if (HttpUtil.is100ContinueExpected(fullHttpRequest)) {
                                        FullHttpResponse response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
                                        ctx.writeAndFlush(response);
                                    }

                                    // 构造 HTTP 响应（版本 + 状态码）
                                    FullHttpResponse response = new DefaultFullHttpResponse(
                                            HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

                                    // 设置响应内容
                                    String content = "Hello, this is a simple Netty HTTP server!";
                                    response.content().writeBytes(content.getBytes());

                                    // 设置 HTTP 头信息(设置响应体类型和长度，防止客户端无限读取)
                                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
                                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

                                    // 返回响应
                                    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                            });

/*                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    // http请求分为请求头和请求体
                                    if(msg instanceof HttpRequest) {
                                        System.out.println("获取到请求头: " + msg);
                                    } else if(msg instanceof HttpContent) {
                                        System.out.println("获取到请求体: " + msg);
                                    }
                                    super.channelRead(ctx, msg);
                                }
                            });*/
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("连接已建立，正在监听8080");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new HttpServer(8080).start();
    }
}

