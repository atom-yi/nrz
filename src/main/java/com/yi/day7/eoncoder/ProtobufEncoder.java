package com.yi.day7.eoncoder;

import com.yi.day7.proto.ProtoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProtobufEncoder extends MessageToByteEncoder<ProtoMessage.Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMessage.Message msg, ByteBuf out) {
        byte[] bytes = msg.toByteArray();
        int length = bytes.length;
        out.writeShort(length);
        out.writeBytes(bytes);
    }
}
