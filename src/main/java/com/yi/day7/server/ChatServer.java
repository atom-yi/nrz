package com.yi.day7.server;

import com.yi.day7.eoncoder.ProtobufDecoder;
import com.yi.day7.eoncoder.ProtobufEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatServer {
    private int port;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;
    private ServerBootstrap bootstrap;

    public void init(int port) {
        this.port = port;
        this.bootstrap = new ServerBootstrap();
    }

    public void run() {
        bossGroup = new NioEventLoopGroup(1);
        workGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ProtobufEncoder())
                                .addLast(new ProtobufDecoder())
                                .addLast("login", ServerContext.loginHandler)
                                .addLast(ServerContext.exceptionHandler);
                    }
                });
        try {
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println("服务启动成功");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
