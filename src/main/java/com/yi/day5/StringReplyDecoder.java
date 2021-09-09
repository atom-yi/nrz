package com.yi.day5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class StringReplyDecoder extends ReplayingDecoder<StringReplyDecoder.State> {
    private int length;
    private byte[] bytes;
    public StringReplyDecoder() {
        super(State.PARSE1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        switch(state()) {
            case PARSE1:
                length = in.readInt();
                bytes = new byte[length];
                checkpoint(State.PARSE2);
                break;
            case PARSE2:
                in.readBytes(bytes, 0, length);
                out.add(new String(bytes, StandardCharsets.UTF_8));
                checkpoint(State.PARSE1);
                break;
            default:
                break;
        }
    }

    enum State {
        PARSE1, PARSE2
    }
}
