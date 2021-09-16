package com.yi.day7;

import com.yi.day7.proto.MessageProto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufEncoder extends MessageToByteEncoder<MessageProto.Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProto.Message msg, ByteBuf out) {
        byte[] bytes = msg.toByteArray();
        int length = bytes.length;
        out.writeShort(length);
        out.writeBytes(bytes);
    }
}
