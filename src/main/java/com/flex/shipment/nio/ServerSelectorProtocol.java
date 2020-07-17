package com.flex.shipment.nio;

import com.flex.shipment.util.ByteUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Description: server handler
 * @Author: flex
 * @Date: 11:17 2020/7/16
 */
public class ServerSelectorProtocol {

    public void handleAccept(SelectionKey key,String responseInfo,int bufSize)throws IOException{
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.write(ByteBuffer.wrap(responseInfo.getBytes("UTF-8")));
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
    }

    public Object handleRead(SelectionKey key)throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] bytes = new byte[0];
        int len;
        while ((len = channel.read(byteBuffer)) > 0) {
            byte[] bytes2 = byteBuffer.array();
            System.out.println(len);
            bytes = ByteUtil.byteMergerByLength(bytes, bytes.length, bytes2, len);
            System.out.println(bytes.length);
            byteBuffer.clear();
        }
        System.out.println("handleRead bytes:"+bytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Object o = objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();
        channel.close();
        return o;
    }


}
