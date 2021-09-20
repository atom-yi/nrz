package com.yi.day7.client.handler;

import com.yi.day7.ProtoInstant;
import com.yi.day7.client.ClientContext;
import com.yi.day7.client.ClientSession;
import com.yi.day7.proto.ProtoMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

@ChannelHandler.Sharable
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMessage.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.Message message = (ProtoMessage.Message) msg;
        ProtoMessage.HeadType headType = message.getType();
        if (!headType.equals(ProtoMessage.HeadType.LOGIN_RESPONSE)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMessage.LoginResponse info = message.getLoginResponse();
        ProtoInstant.ResultCodeEnum result = ProtoInstant.ResultCodeEnum.fromCode(info.getCode());
        if (!result.equals(ProtoInstant.ResultCodeEnum.SUCCESS)) {
            System.out.println(result.getDesc());
        } else {
            ClientSession.loginSuccess(ctx, message);
            ChannelPipeline pipeline = ctx.pipeline();
            pipeline.remove(this);
            pipeline.addAfter("encoder", "heartbeat", ClientContext.heartBeatHandler);
            pipeline.addAfter("encoder", "chat", ClientContext.chatMsgHandler);

            ClientContext.heartBeatHandler.channelActive(ctx);
        }
    }
}
