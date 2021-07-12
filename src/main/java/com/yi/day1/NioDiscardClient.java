package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class NioDiscardClient {

    public static void main(String[] args) throws IOException {
        startClient();
    }

    public static void startClient() throws IOException {
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        SocketChannel channel = SocketChannel.open(address);
        channel.configureBlocking(false);
        while (!channel.finishConnect()) {
            // 不断自旋
        }
        log.info("连接服务器成功");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put("hello world".getBytes(StandardCharsets.UTF_8));
        buffer.flip();
        // 发送到服务器
        channel.write(buffer);
        channel.shutdownOutput();
        channel.close();
    }

}
