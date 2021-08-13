package com.yi.day2.single;


import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class EchoClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        SocketChannel sc = SocketChannel.open();
        sc.configureBlocking(false);
        sc.connect(new InetSocketAddress(8080));
        Selector selector = Selector.open();
        while (!sc.finishConnect()) {
            TimeUnit.MILLISECONDS.sleep(300);
        }
        System.out.println("链接服务器成功");

        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Scanner sysInput = new Scanner(System.in);
        while (selector.select() > 0) {
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isReadable()) {
                    System.out.println("可读");
                    int length;
                    while ((length = sc.read(buffer)) > 0) {
                        buffer.flip();
                        System.out.println(new String(buffer.array(), 0, length));
                    }
                } else if (key.isWritable()) {
                    System.out.println("可写");
                    String str = sysInput.nextLine();
                    buffer.clear();
                    buffer.put(str.getBytes(StandardCharsets.UTF_8));
                    buffer.clear();
                }
                iterator.remove();
            }
        }

        sc.shutdownInput();
        sc.shutdownOutput();
        sc.close();
        System.out.println("程序结束");
    }

}
