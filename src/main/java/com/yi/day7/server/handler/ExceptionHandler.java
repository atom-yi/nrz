package com.yi.day7.server.handler;

import com.yi.day7.exception.InvalidFrameException;
import com.yi.day7.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.IOException;

@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof InvalidFrameException) {
            cause.printStackTrace();
            System.out.println(cause.getMessage());
        } else if (cause instanceof IOException) {
            System.out.println(cause.getMessage());
            System.out.println("客户端连接已关闭");
            ServerSession.closeSession(ctx);
        } else {
            cause.printStackTrace();
            System.out.println(cause.getMessage());
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ServerSession.closeSession(ctx);
    }
}
