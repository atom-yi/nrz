package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class UDPServer {

    public static void main(String[] args) throws IOException {
        new UDPServer().receive();
    }

    public void receive() throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(8888));
        log.info("UDP 服务器启动成功");
        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_READ);
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    // 有可读数据
                    SocketAddress client = channel.receive(buffer);
                    buffer.flip();
                    log.info(new String(buffer.array(), 0, buffer.limit()));
                    buffer.clear();
                }
            }
            iterator.remove();
        }
        selector.close();
        channel.close();
    }

}
