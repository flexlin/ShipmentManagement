package com.flex.shipment.client;

import com.flex.shipment.nio.ClientHandler;
import com.flex.shipment.pojo.Trade;
import java.nio.channels.Selector;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: client to service communication
 * @Author: flex
 * @Date: 14:03 2020/7/14
 */
public class SubmitTrade {

    public void submit(Trade trade, String host, int port)throws Exception{
        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        ClientHandler handler = new ClientHandler();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host, port));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        boolean flag = true;
        while (flag) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        System.out.println("handleConnect!");
                        handler.handleConnect(key, trade, 1024);
                    }
                    if (key.isReadable()) {
                        System.out.println("isReadable!");
                        handler.handleRead(key);
                        key.channel().close();
                        flag = false;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        socketChannel.close();
        selector.close();
    }


}
