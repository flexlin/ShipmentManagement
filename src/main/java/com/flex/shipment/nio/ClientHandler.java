package com.flex.shipment.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @Description: client handler
 * @Author: flex
 * @Date: 12:09 2020/7/16
 */
public class ClientHandler {

    public void handleConnect(SelectionKey key,Object obj,int bufSize)throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()){
            try {
                boolean b = channel.finishConnect();
                System.out.println("finishConnect:"+b);
            }catch (Exception e){
                System.out.println("finishConnect error!");
            }
        }

        SocketChannel socketChannel = (SocketChannel) key.channel();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        byte[] bs = baos.toByteArray();
        System.out.println(bs.length);
        ByteBuffer buffer = ByteBuffer.wrap(bs, 0, bs.length);
        socketChannel.write(buffer);
        baos.close();
        oos.close();

    }

    public String handleRead(SelectionKey key)throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        int len;
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        StringBuilder sb = new StringBuilder();
        while ((len = channel.read(byteBuffer)) > 0) {
            byte[] bytes = byteBuffer.array();
            sb.append(new String(bytes, 0, len,"UTF-8"));
            byteBuffer.clear();
        }
        System.out.println("get message from server: " + sb);
        channel.close();
        return sb.toString();
    }



}
