package com.yi.day4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class NettyEchoClient {
    private final int serverPort;
    private final String ip;
    Bootstrap bootstrap = new Bootstrap();

    public NettyEchoClient(String ip, int port) {
        this.ip = ip;
        this.serverPort = port;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(ip, serverPort)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(NettyEchoClientHandler.INSTANCE);
                    }
                });

        ChannelFuture future = bootstrap.connect();
        future.addListener(listener -> log.info("EchoClient 客户端连接{}", listener.isSuccess() ? "成功" : "失败"));

        try {
            future.sync();
            process(future.channel());
        } catch (InterruptedException e) {
            log.info("occurred error", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    private void process(Channel channel) {
        Scanner sc = new Scanner(System.in);
        log.info("请输入发送内容: ");
        while (sc.hasNext()) {
            String next = sc.next();
            byte[] bytes = (">>" + next).getBytes(StandardCharsets.UTF_8);
            ByteBuf buffer = channel.alloc().buffer();
            buffer.writeBytes(bytes);
            channel.writeAndFlush(buffer);
            log.info("请输入发送内容");
        }
    }

    public static void main(String[] args) {
        new NettyEchoClient("localhost", 8080).run();
    }
}
