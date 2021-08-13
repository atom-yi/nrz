package com.yi.day2.single;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoHandler implements Runnable {

    Selector selector;
    SocketChannel sc;
    SelectionKey sk;
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    static final int RECEIVING = 0, SENDING = 1;
    int state = RECEIVING;

    public EchoHandler(Selector selector, SocketChannel channel) throws IOException {
        this.selector = selector;
        this.sc = channel;

        sc.configureBlocking(false);
        sk = sc.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            if (state == SENDING) {
                sc.write(buffer);
                buffer.flip();
                sk.interestOps(SelectionKey.OP_READ);
                state = RECEIVING;
            } else if (state == RECEIVING) {
                int length = 0;
                while ((length = sc.read(buffer)) > 0) {
                    System.out.println(new String(buffer.array(), 0, length));
                }
                buffer.flip();
                sk.interestOps(SelectionKey.OP_WRITE);
                state = RECEIVING;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
