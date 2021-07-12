package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class NioDiscardServer {

    public static void main(String[] args) throws IOException {
        startServer();
    }

    public static void startServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress("127.0.0.1", 8888));

        log.info("通道建立成功");
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (selector.select() > 0) {
            Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    // 建立新的连接
                    SocketChannel channel = ssc.accept();
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } else if (key.isReadable()) {
                    // 有可读数据
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int length = 0;
                    while ((length = channel.read(buffer)) > 0) {
                        buffer.flip();
                        log.info(new String(buffer.array(), 0, length));
                        buffer.clear();
                    }
                    channel.close();
                }
            }
            iter.remove();
        }
    }

}
