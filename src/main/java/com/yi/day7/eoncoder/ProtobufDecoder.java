package com.yi.day7.eoncoder;

import com.google.protobuf.InvalidProtocolBufferException;
import com.yi.day7.proto.ProtoMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ProtobufDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws InvalidProtocolBufferException {
        in.markReaderIndex();
        if (in.readableBytes() < 2) {
            return;
        }

        int length = in.readShort();
        if (length < 0) {
            // 非法数据
            ctx.close();
        }

        if (length > in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }

        byte[] data;
        if (in.hasArray()) {
            // 堆缓冲
            ByteBuf slice = in.slice();
            data = slice.array();
        } else {
            // 直接内存缓冲
            data = new byte[length];
            in.readBytes(data, 0, length);
        }

        ProtoMessage.Message msg = ProtoMessage.Message.parseFrom(data);
        if (msg != null) {
            out.add(msg);
        }
    }
}
