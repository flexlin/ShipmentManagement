package com.flex.shipment.nio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import java.nio.ByteBuffer;

/**
 * @Description:
 * @Author: flex
 * @Date: 13:35 2020/7/16
 */
public class ByteBufferTest {

    @Test
    public void test(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byte[] bytes = "hello".getBytes();
        System.out.println(bytes.length);
        byteBuffer.put("hello".getBytes());
        ByteBuffer byteBuffer2 = ByteBuffer.allocate(20);
//        byteBuffer.put(byteBuffer2);
        System.out.println(byteBuffer);

    }

    @Test
    public void testOne(){
        byte[] bytes = new byte[0];
        System.out.println(bytes.length);
        System.out.println();
    }


}
