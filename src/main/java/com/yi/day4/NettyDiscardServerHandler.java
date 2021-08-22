package com.yi.day4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyDiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        try {
            log.info("接收到消息，丢弃消息：");
            while (buf.isReadable()) {
                System.out.print((char)buf.readByte());
            }
            System.out.println();
        } finally {
            ReferenceCountUtil.release(buf);
        }
    }

}
