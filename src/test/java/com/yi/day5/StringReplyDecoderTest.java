package com.yi.day5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class StringReplyDecoderTest {
    @Test
    public void testDecode() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> ini = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new StringReplyDecoder())
                        .addLast(new StringProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(ini);
        writeMessage(channel);
        TimeUnit.SECONDS.sleep(2);
    }

    private void writeMessage(EmbeddedChannel channel) {
        String[] messages = new String[]{"hello", "dududu", "ping pong"};
        for (String message : messages) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(message.length());
            buf.writeBytes(message.getBytes(StandardCharsets.UTF_8));
            channel.writeInbound(buf);
        }
    }
}
