package com.yi.day6;

import com.yi.day6.proto.MsgProto;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProtoBufServer {
    private final int port;
    private final ServerBootstrap bootstrap;

    public ProtoBufServer(int port) {
        this.port = port;
        bootstrap = new ServerBootstrap();
    }

    public void run() {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        bootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast(new ProtobufDecoder(MsgProto.Msg.getDefaultInstance()));
                        ch.pipeline().addLast(new ProtobufBusinessDecoder());
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

    static class ProtobufBusinessDecoder extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            MsgProto.Msg message = (MsgProto.Msg) msg;
            log.info("接收到消息，id=>{} content={}", message.getId(), message.getContent());
        }
    }

    public static void main(String[] args) {
        new ProtoBufServer(8080).run();
    }
}
