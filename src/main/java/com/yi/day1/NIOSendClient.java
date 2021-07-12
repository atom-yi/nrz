package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * NIO 传输文件 - 客户端
 */
@Slf4j
public class NIOSendClient {

    public static void main(String[] args) throws Exception {
        NIOSendClient client = new NIOSendClient();
        client.sendFile();
    }

    private File srcFile = new File("/Users/junyiwu/workspace/code/code/java/nrz/src/main/resources/temp.txt");
    private File destFile = new File("/Users/junyiwu/workspace/code/code/java/nrz/src/main/resources/temp1.txt");
    public void sendFile() throws Exception {
        if (!srcFile.exists()) {
            log.info("源文件文件不存在，路径：{}", srcFile.getAbsolutePath());
        }
        FileChannel fileChannel = new FileInputStream(srcFile).getChannel();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.socket().connect(new InetSocketAddress(8888));
        socketChannel.configureBlocking(false);

        while (!socketChannel.finishConnect()) {
            // 自旋等待连接成功
        }
        log.info("连接服务器成功");

        // 发送文件名称
        ByteBuffer fileNameBuffer = StandardCharsets.UTF_8.encode(destFile.getAbsolutePath());
        socketChannel.write(fileNameBuffer);
        // 发送文件长度
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putLong(srcFile.length());
        buffer.flip();
        socketChannel.write(buffer);

        log.info("开始传输文件");
        int length = 0;
        long process = 0;
        while ((length = fileChannel.read(buffer)) > 0) {
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
            process += length;
            log.info("| {}% |", (100 * process / srcFile.length()));
        }
        if (length == -1) {
            fileChannel.close();
            socketChannel.shutdownOutput();
            socketChannel.close();
        }
        log.info("文件传输完成");
    }

}
