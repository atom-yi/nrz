package com.yi.day5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class IntegerAddDecoderTest {
    @Test
    public void testDecode() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> ini = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new IntegerAddDecoder())
                .addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(ini);
        for (int i = 0; i < 10; i++) {
            ByteBuf buffer = Unpooled.buffer();
            buffer.writeInt(i);
            channel.writeInbound(buffer);
        }
        TimeUnit.SECONDS.sleep(2);
    }
}
