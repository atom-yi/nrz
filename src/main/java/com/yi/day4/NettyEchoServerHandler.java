package com.yi.day4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
@ChannelHandler.Sharable
public class NettyEchoServerHandler extends ChannelInboundHandlerAdapter {
    public static final NettyEchoServerHandler INSTANCE = new NettyEchoServerHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        log.info("消息类型：{}", (buf.hasArray() ? "堆内存" : "直接内存"));
        int len = buf.readableBytes();
        byte[] arr = new byte[len];
        buf.getBytes(0, arr);
        log.info("server received: {}", new String(arr, StandardCharsets.UTF_8));

        // 写回
        log.info("写回前, msg.refCnt:{}", buf.refCnt());
        ChannelFuture future = ctx.writeAndFlush(msg);
        future.addListener(listener -> log.info("写回后, msg.refCnt:{}", buf.refCnt()));
    }
}
