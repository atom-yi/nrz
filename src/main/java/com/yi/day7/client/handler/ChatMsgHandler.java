package com.yi.day7.client.handler;

import com.yi.day7.proto.ProtoMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class ChatMsgHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMessage.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.Message pkg = (ProtoMessage.Message) msg;
        ProtoMessage.HeadType headType = pkg.getType();
        if (!headType.equals(ProtoMessage.HeadType.MESSAGE_REQUEST)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.MessageRequest req = pkg.getMessageRequest();
        System.out.println("接收到消息 from uid: " + req.getFrom() + " -> " + req.getContent());
    }
}
