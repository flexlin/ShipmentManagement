package com.flex.shipment.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: flex
 * @Date: 12:09 2020/7/16
 */
public class ClientHandler {

    public void handleConnect(SelectionKey key,Object obj,int bufSize)throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()){
            boolean b = channel.finishConnect();
            System.out.println("finishConnect:"+b);
        }

        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_WRITE, ByteBuffer.allocate(bufSize));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);

        byte[] bs = baos.toByteArray();
        System.out.println(bs.length);
        ByteBuffer buffer = ByteBuffer.wrap(bs, 0, bs.length);
//        buffer.flip();
        boolean remaining = buffer.hasRemaining();
        System.out.println("remaining:" +remaining);
        socketChannel.write(buffer);
        buffer.flip();
        baos.close();
        oos.close();
    }

    public String handleRead(SelectionKey key)throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        byte[] bytes = new byte[1024];
        int len;
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        StringBuilder sb = new StringBuilder();
        while ((len = channel.read(byteBuffer)) != -1) {
            //charset UTF-8
            sb.append(new String(bytes, 0, len,"UTF-8"));
        }
        System.out.println("get message from server: " + sb);

        return sb.toString();
    }



}
