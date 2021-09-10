package com.yi.day5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class NettyOpenBoxDecoderTest {
    public static final int VERSION = 201;

    @Test
    public void testDecode() throws InterruptedException {
        final LengthFieldBasedFrameDecoder decoder =
                new LengthFieldBasedFrameDecoder(1024, 0, 4, 2, 6);
        ChannelInitializer<EmbeddedChannel> ini = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(decoder).addLast(new StringDecoder(StandardCharsets.UTF_8))
                        .addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(ini);
        byte[] content = "hello, This is Yi~".getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < 5; i++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(content.length);
            buf.writeChar(VERSION);
            buf.writeBytes(content);
            channel.writeInbound(buf);
        }
        TimeUnit.SECONDS.sleep(2);
    }
}
