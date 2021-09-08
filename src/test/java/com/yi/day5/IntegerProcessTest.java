package com.yi.day5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class IntegerProcessTest {

    @Test
    public void testIntegerProcess() throws InterruptedException {
        ChannelInitializer<EmbeddedChannel> ini = new ChannelInitializer<EmbeddedChannel>() {
            @Override
            protected void initChannel(EmbeddedChannel ch) {
                ch.pipeline().addLast(new Byte2IntegerDecoder())
                        .addLast(new IntegerProcessHandler());
            }
        };
        EmbeddedChannel channel = new EmbeddedChannel(ini);
        for (int i = 0; i < 10; i++) {
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(i);
            channel.writeInbound(buf);
        }
        TimeUnit.SECONDS.sleep(3);
    }

}
