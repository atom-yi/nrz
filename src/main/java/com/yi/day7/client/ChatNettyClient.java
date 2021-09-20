package com.yi.day7.client;

import com.yi.day7.eoncoder.ProtobufDecoder;
import com.yi.day7.eoncoder.ProtobufEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;

@Data
public class ChatNettyClient {
    private String host;
    private int port;
    private boolean connectedFlag;
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private GenericFutureListener<ChannelFuture> connectedListener;

    public ChatNettyClient() {
        group = new NioEventLoopGroup();
    }

    public void init(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void doConnect() {
        try {
            bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .remoteAddress(host, port);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast("decoder", new ProtobufDecoder())
                            .addLast("encoder", new ProtobufEncoder())
                            .addLast(ClientContext.loginHandler)
                            .addLast(ClientContext.exceptionHandler);
                }
            });
            System.out.println("客户端开始建立连接");
            ChannelFuture future = bootstrap.connect();
            future.addListener(connectedListener);
        } catch (Exception e) {
            System.out.println("客户端连接失败");
        }
    }

    public void close() {
        group.shutdownGracefully();
    }
}
