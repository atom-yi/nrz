package com.yi.day6;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext context, Object obj) {
        String json = (String) obj;
        JsonMsg msg = JsonMsg.parse(json);
        log.info("received from client => \n{}", msg);
    }
}
