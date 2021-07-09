package com.yi.day1;

import lombok.extern.slf4j.Slf4j;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public class UseChannel {

    public static void main(String[] args) throws IOException {
//        testFileChannelWrite();
//        testFileChannelRead();
        testFileNIOCopy();
    }

    private static File file = new File("/Users/junyiwu/workspace/code/java/nrz/src/main/resources/temp.txt");
    public static void testFileChannelRead() throws IOException {
        FileInputStream stream = new FileInputStream(file);
        FileChannel channel = stream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(20);
        while (channel.read(buffer) != -1) {
            System.out.print(new String(buffer.array(), StandardCharsets.UTF_8));
            buffer.clear();
        }
        channel.close();
        System.out.println();
    }

    public static void testFileChannelWrite() {
        try (FileChannel channel = new FileOutputStream(file).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put("hello".getBytes(StandardCharsets.UTF_8));
            int num = 0;
            buffer.flip();
            while ((num = channel.write(buffer)) != 0) {
                log.info("写入{}字节", num);
            }
            channel.force(true);  // 可能并不会立即更新到磁盘中去，需要强制刷新一下
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File targetFile = file;
    private static File srcFile = new File("/Users/junyiwu/workspace/code/java/nrz/src/main/resources/log4j2.xml");
    public static void testFileNIOCopy() throws IOException {
        FileInputStream input = new FileInputStream(srcFile);
        FileOutputStream output = new FileOutputStream(targetFile);
        FileChannel srcChannel = input.getChannel();
        FileChannel targetChannel = output.getChannel();
        try {
            targetChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            srcChannel.close();
            targetChannel.close();
            output.close();
            input.close();
        }
    }

}
