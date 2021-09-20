package com.yi.day7.client.handler;

import com.yi.day7.client.ClientContext;
import com.yi.day7.client.ClientSession;
import com.yi.day7.exception.InvalidFrameException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        if (cause instanceof InvalidFrameException) {
            ClientSession.getSession(ctx).close();
        } else {
            ctx.close();

            // 重连
            ClientContext.nettyClient.setConnectedFlag(false);
            ClientContext.nettyClient.doConnect();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
