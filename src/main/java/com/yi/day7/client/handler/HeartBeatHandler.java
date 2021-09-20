package com.yi.day7.client.handler;

import com.yi.day7.client.ClientSession;
import com.yi.day7.client.protoBuilder.HeartBeatMsgBuilder;
import com.yi.day7.pojo.User;
import com.yi.day7.proto.ProtoMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    // 心跳间隔
    private static final int HEARTBEAT_INTERVAL = 50;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        ProtoMessage.Message msg = new HeartBeatMsgBuilder(user, session).buildMsg();
        heartBeat(ctx, msg);
    }

    public void heartBeat(ChannelHandlerContext ctx, ProtoMessage.Message msg) {
        ctx.executor().schedule(() -> {
            System.out.println("向服务端发送心跳信息");
            ctx.writeAndFlush(msg);
            heartBeat(ctx, msg);
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMessage.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.Message pkg = (ProtoMessage.Message) msg;
        ProtoMessage.HeadType headType = pkg.getType();
        if (!headType.equals(ProtoMessage.HeadType.KEEPALIVE_REQUEST)) {
            super.channelRead(ctx, msg);
            return;
        }

        System.out.println("从服务端接收到心跳确认");
    }
}
