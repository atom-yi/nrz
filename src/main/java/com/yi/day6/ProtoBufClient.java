package com.yi.day6;

import com.yi.day6.proto.MsgProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

@Slf4j
public class ProtoBufClient {
    private final String serverHost;
    private final int serverPort;
    private final Bootstrap bootstrap;
    public ProtoBufClient(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.bootstrap = new Bootstrap();
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .remoteAddress(serverHost, serverPort)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtobufEncoder());
                    }
                });

        ChannelFuture future = bootstrap.connect();
        future.addListener(listener -> log.info("EchoClient 客户端连接{}", listener.isSuccess() ? "成功" : "失败"));

        try {
            Channel channel = future.sync().channel();
            for (int i = 0; i < 5; i++) {
                MsgProto.Msg msg = randomMsg();
                channel.writeAndFlush(msg);
                log.info("发送 id=>{} content=>{}", msg.getId(), msg.getContent());
            }
            log.info("发送完成.");
        } catch (InterruptedException e) {
            log.info("occurred error", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    private MsgProto.Msg randomMsg() {
        MsgProto.Msg.Builder builder = MsgProto.Msg.newBuilder();
        builder.setId(RandomUtils.nextInt());
        builder.setContent(RandomStringUtils.random(10));
        return builder.build();
    }

    public static void main(String[] args) {
        new ProtoBufClient("localhost", 8080).run();
    }
}
