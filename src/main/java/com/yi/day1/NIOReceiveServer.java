package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class NIOReceiveServer {

    public static void main(String[] args) throws IOException {
        NIOReceiveServer server = new NIOReceiveServer();
        server.startServer();
    }

    static class Client {
        String fileName; // 文件名称
        long fileLength; // 文件长度
        long startTime; // 传输开始时间
        InetSocketAddress removeAddress; // 客户端地址
        FileChannel outChannel; // 输出的文件管道
    }

    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    Map<SelectableChannel, Client> clientMap = new HashMap<>(); // 保存每个文件传输
    private String receivePath = "/Users/junyiwu/workspace/code/java/nrz/src/main/resources/receive";

    public void startServer() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        ServerSocket ss = channel.socket();
        channel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
        channel.bind(address);
        channel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("服务开始监听:{}", address);
        while (selector.select() > 0) {
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    if (sc == null) {
                        continue;
                    }
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);

                    Client client = new Client();
                    client.removeAddress = (InetSocketAddress) sc.getRemoteAddress();
                    clientMap.put(sc, client);
                } else if (key.isReadable()) {
                    processData(key);
                }

                iterator.remove();
            }
        }
    }

    // 处理客户端发送过来的数据
    private void processData(SelectionKey key) throws IOException {
        Client client = clientMap.get(key.channel());
        SocketChannel sc  = (SocketChannel) key.channel();
        int num = 0;
        try {
            buffer.clear();
            while ((num = sc.read(buffer)) > 0) {
                buffer.flip();
                if (null == client.fileName) {
                    String fileName = StandardCharsets.UTF_8.decode(buffer).toString();
                    File directory = new File(receivePath);
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    File destFile = new File(receivePath + File.separator + fileName);
//                    if (!destFile.exists()) {
//                        boolean succ = destFile.createNewFile();
//                        log.info("文件创建{}", succ ? "成功" : "失败");
//                    }
                    FileChannel fileChannel = new FileOutputStream(destFile).getChannel();
                    client.outChannel = fileChannel;
                    log.info("接收文件地址: {}", destFile.getAbsolutePath());
                } else if (0 == client.fileLength) {
                    long fileLength = buffer.getLong();
                    client.fileLength = fileLength;
                    client.startTime = System.currentTimeMillis();
                    log.info("传输开始...");
                } else {
                    client.outChannel.write(buffer);
                }
                buffer.clear();
            }
            key.cancel();
        } catch (IOException e) {
            key.cancel();
            e.printStackTrace();
            return;
        }
        if (num == -1) {
            if (client.outChannel != null) {
                client.outChannel.close();
            }
            log.info("文件接收成功，文件名称：{}", client.fileName);
            log.info("文件大小：{}b", client.fileLength);
            log.info("耗时：{}ms", System.currentTimeMillis() - client.startTime);
        }
    }

}
