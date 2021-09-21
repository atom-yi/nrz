package com.yi.day7.server.handler;

import com.yi.day7.concurrent.FutureTaskScheduler;
import com.yi.day7.proto.ProtoMessage;
import com.yi.day7.server.ServerContext;
import com.yi.day7.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ChatRedirectHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMessage.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.Message pkg = (ProtoMessage.Message) msg;
        ProtoMessage.HeadType headType = ((ProtoMessage.Message) msg).getType();
        if (!headType.equals(ServerContext.chatRedirectProcessor.type())) {
            super.channelRead(ctx, msg);
            return;
        }

        ServerSession session = ServerSession.getSession(ctx);
        if (session == null || !session.isLogin()) {
            System.out.println("用户尚未登录，不能发送消息");
            return;
        }

        FutureTaskScheduler.add(() -> {
            ServerContext.chatRedirectProcessor.action(session, pkg);
        });
    }
}
