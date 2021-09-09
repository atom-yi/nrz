package com.yi.day5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class IntegerAddDecoder extends ReplayingDecoder<IntegerAddDecoder.State> {
    enum State {
        PARSE_1, PARSE_2
    }
    private int first;
    private int second;
    public IntegerAddDecoder() {
        super(State.PARSE_1);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        switch(state()) {
            case PARSE_1:
                first = in.readInt();
                checkpoint(State.PARSE_2);
                break;
            case PARSE_2:
                second = in.readInt();
                Integer sum = first + second;
                out.add(sum);
                checkpoint(State.PARSE_1);
                break;
            default:
        }
    }
}
