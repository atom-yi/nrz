package com.yi.day1;

import lombok.extern.slf4j.Slf4j;

import java.nio.IntBuffer;

@Slf4j
public class UseBuffer {

    public static void main(String[] args) {
//        allocateTest();
        putTest();
    }

    public static void allocateTest() {
        IntBuffer buffer = IntBuffer.allocate(20);
        log.info("capacity => {}", buffer.capacity());
        log.info("limit => {}", buffer.limit());
        log.info("position => {}", buffer.position());
    }

    public static void putTest() {
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < 5; i++) {
            buffer.put(i);
        }
        log.info("capacity => {}", buffer.capacity());
        log.info("limit => {}", buffer.limit());
        log.info("position => {}", buffer.position());
        buffer.flip();
        log.info("limit => {}", buffer.limit());
        log.info("position => {}", buffer.position());
    }

}
