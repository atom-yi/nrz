package com.yi.day7.server.handler;

import com.yi.day7.concurrent.CallbackTask;
import com.yi.day7.concurrent.CallbackTaskScheduler;
import com.yi.day7.proto.ProtoMessage;
import com.yi.day7.server.ServerContext;
import com.yi.day7.server.ServerSession;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class LoginHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMessage.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.Message pkg = (ProtoMessage.Message) msg;
        ProtoMessage.HeadType headType = pkg.getType();
        if (!headType.equals(ServerContext.loginProcessor.type())) {
            super.channelRead(ctx, msg);
            return;
        }

        ServerSession session = new ServerSession(ctx.channel());
        CallbackTaskScheduler.add(new CallbackTask<Boolean>() {

            @Override
            public Boolean execute() {
                return ServerContext.loginProcessor.action(session, pkg);
            }

            @Override
            public void onBack(Boolean r) {
                if (r) {
                    ctx.pipeline()
                            .addAfter("login", "chat", ServerContext.chatRedirectHandler)
                            .addAfter("login", "heartBeat", new HeartBeatServerHandler());
                    ctx.pipeline().remove("login");
                    System.out.println("登录成功: " + session.getUser());
                } else {
                    ServerSession.closeSession(ctx);
                    System.out.println("登录失败：" + session.getUser());
                }
            }

            @Override
            public void onException(Throwable t) {
                ServerSession.closeSession(ctx);
                System.out.println("登录失败：" + session.getUser());
            }
        });
    }
}
