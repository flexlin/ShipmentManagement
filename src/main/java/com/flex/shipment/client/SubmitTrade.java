package com.flex.shipment.client;

import com.flex.shipment.nio.ClientHandler;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description: client to service communication
 * @Author: flex
 * @Date: 14:03 2020/7/14
 */
public class SubmitTrade {

    public void submit(Trade trade, String host, int port)throws Exception{
        /*// server connection
        Socket socket = new Socket(host, port);
        // outputStream
        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(trade);
        //through shutdownOutput send data，After only read data
        socket.shutdownOutput();

        // inputStream
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //charset UTF-8
            sb.append(new String(bytes, 0, len,"UTF-8"));
        }
        System.out.println("get message from server: " + sb);

        inputStream.close();
        outputStream.close();
        socket.close();*/

        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        ClientHandler handler = new ClientHandler();
        socketChannel.configureBlocking(false);
        socketChannel.connect(new InetSocketAddress(host,port));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            try {
                selector.select(1000);
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        System.out.println("handleConnect!");
                        handler.handleConnect(key, trade, 1024);
                    }
                    if (key.isReadable()) {
                        handler.handleRead(key);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }


}
