package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
public class UDPClient {

    public static void main(String[] args) throws IOException {
        new UDPClient().send();
    }

    public void send() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        log.info("客户端启动成功");
        log.info("请输入发送的消息：");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            buffer.put((">>" + line).getBytes(StandardCharsets.UTF_8));
            buffer.flip();
            channel.send(buffer, new InetSocketAddress("127.0.0.1", 8888));
            buffer.clear();
        }
        channel.close();
    }

}
