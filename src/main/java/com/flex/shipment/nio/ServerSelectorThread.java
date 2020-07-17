package com.flex.shipment.nio;

import com.flex.shipment.manage.Scheduler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: flex
 * @Date: 19:02 2020/7/17
 */
public class ServerSelectorThread extends Thread{

    private LinkedBlockingQueue<SelectionKey> keys;
    private ServerSelectorProtocol protocol = new ServerSelectorProtocol();
    private Scheduler scheduler;

    public ServerSelectorThread(LinkedBlockingQueue<SelectionKey> keys, Scheduler scheduler){
        this.keys = keys;
        this.scheduler = scheduler;
    }

    @Override
    public void run() {
        while (true) {
            SelectionKey key = null;
            try {
                key = keys.poll();
                if (key != null) {
                    if (key.isAcceptable()) {
                        System.out.println("isAcceptable!");
                        protocol.handleAccept(key, "Hello Client,I get the message.", 1024);
                    }
                    if (key.isReadable()) {
                        Object o = protocol.handleRead(key);
                        System.out.println("scheduler receive a Object!");
                        scheduler.receiver(o);
                    }
                }
            }catch (Exception e){
                if(key !=null){
                    key.cancel();
                    if(key.channel() !=null) {
                        try {
                            key.channel().close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
                e.printStackTrace();
            }
        }

    }
}
