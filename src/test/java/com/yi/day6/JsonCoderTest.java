package com.yi.day6;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JsonCoderTest {
    static EmbeddedChannel serverChannel;

    @Test
    public void testJsonCoder() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> serverIni = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                        .addLast(new StringDecoder())
                        .addLast(new JsonDecoder());
            }
        };
        serverChannel = new EmbeddedChannel(serverIni);

        ChannelInitializer<EmbeddedChannel> clientIni = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline()
                        .addLast(new LengthFieldPrepender(4))
                        .addLast(new StringEncoder(StandardCharsets.UTF_8))
                        .addFirst(new JsonDecoder());
            }
        };
        EmbeddedChannel clientChannel = new EmbeddedChannel(clientIni);
        clientChannel.writeOutbound(buildJson().toJson());

        TimeUnit.SECONDS.sleep(2);
    }

    static class JsonDecoder extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            log.info("write msg");
            ByteBuf clone = ((ByteBuf) msg).duplicate();
            serverChannel.writeInbound(clone);
        }
    }

    private JsonMsg buildJson() {
        JsonMsg msg = new JsonMsg();
        msg.setId(1);
        msg.setContent("llllll");
        return msg;
    }
}
