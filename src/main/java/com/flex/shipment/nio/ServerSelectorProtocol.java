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
 * @Description:
 * @Author: flex
 * @Date: 11:17 2020/7/16
 */
public class ServerSelectorProtocol {

    public void handleAccept(SelectionKey key,String responseInfo,int bufSize)throws IOException{
        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocate(bufSize));
        socketChannel.write(ByteBuffer.wrap(responseInfo.getBytes("UTF-8")));
    }

    public Object handleRead(SelectionKey key)throws Exception{
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        System.out.println("handleRead byteBuffer:"+byteBuffer.array().length);
        Object o = null;
        byte[] bytes = new byte[0];
        int read = channel.read(byteBuffer);
        System.out.println(read);
        while (channel.read(byteBuffer)!=-1) {
            byte[] bytes2 = byteBuffer.array();
            bytes = ByteUtil.byteMerger(bytes, bytes2);
            byteBuffer.flip();
            System.out.println(bytes.length);
        }
        System.out.println("handleRead bytes:"+bytes.length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        o = objectInputStream.readObject();
        objectInputStream.close();
        inputStream.close();
        channel.close();

        return o;
    }


}
