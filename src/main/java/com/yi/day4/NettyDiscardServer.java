package com.yi.day4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyDiscardServer {

    private final int serverPort;
    ServerBootstrap bootstrap = new ServerBootstrap();
    public NettyDiscardServer(int port) {
        serverPort = port;
    }

    public void runServer() {
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(serverPort)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyDiscardServerHandler());
                    }
                });

        try {
            ChannelFuture future = bootstrap.bind().sync();
            ChannelFuture closeFuture = future.channel().closeFuture();
            closeFuture.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        int port = 8080;
        new NettyDiscardServer(port).runServer();
    }
}
