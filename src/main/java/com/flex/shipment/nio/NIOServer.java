package com.flex.shipment.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: flex
 * @Date: 19:18 2020/7/17
 */
public class NIOServer {

    private LinkedBlockingQueue<SelectionKey> keysQueue = new LinkedBlockingQueue<SelectionKey>();
    public LinkedBlockingQueue<SelectionKey> getKeysQueue() {
        return keysQueue;
    }

    public void accept(int port)throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        ServerSelectorProtocol protocol = new ServerSelectorProtocol();
        int i = 0;
        while (true) {
            try {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    System.out.println("accept:" + i++);
                    SelectionKey key = iterator.next();
                    keysQueue.add(key);
                    iterator.remove();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
