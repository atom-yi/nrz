package com.yi.day7.server.handler;

import com.yi.day7.concurrent.FutureTaskScheduler;
import com.yi.day7.proto.ProtoMessage;
import com.yi.day7.server.ServerSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HeartBeatServerHandler extends IdleStateHandler {
    private static final int READ_IDLE_GAP = 150;

    public HeartBeatServerHandler() {
        super(READ_IDLE_GAP, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMessage.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.Message pkg = (ProtoMessage.Message) msg;
        ProtoMessage.HeadType type = pkg.getType();
        if (type.equals(ProtoMessage.HeadType.KEEPALIVE_REQUEST)) {
            FutureTaskScheduler.add(() -> {
                if (ctx.channel().isActive()) {
                    ctx.writeAndFlush(msg);
                }
            });
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READ_IDLE_GAP + "秒未读取到数据，关闭连接");
        ServerSession.closeSession(ctx);
    }
}
