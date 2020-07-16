package com.flex.shipment.manage;

import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import com.flex.shipment.nio.ServerSelectorProtocol;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;

/**
 * @Description: service enter to start
 * @Author: flex
 * @Date: 13:31 2020/7/14
 */
public class ManagerBackend {

    private GoodsFactory goodsFactory = new GoodsFactory();
    private LoaderFactory loaderFactory = new LoaderFactory();
    private SupplierListener supplierListener = new SupplierListener();
    private SupplierManager supplierManager = new SupplierManager(goodsFactory,loaderFactory,supplierListener);
    private TaskManager taskManager = new TaskManager(supplierManager.getOperation());
    private Scheduler scheduler = new Scheduler(supplierListener,supplierManager,taskManager);

    private void init() {
        ExecutorService threadPool = taskManager.getThreadPool();
        threadPool.submit(taskManager.start());
        threadPool.submit(scheduler.start());
        System.out.println("init finished!!!");
    }

    public void start(){
        new Thread(){
            @Override
            public void run() {
                try {
                    init();
                    Selector selector = Selector.open();
                    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                    serverSocketChannel.bind(new InetSocketAddress(10230));
                    serverSocketChannel.configureBlocking(false);
                    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                    ServerSelectorProtocol protocol = new ServerSelectorProtocol();
                    int i = 0;
                    while (true) {
                        SelectionKey key = null;
                        try {
                            selector.select(1000);
                            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                            while (iterator.hasNext()) {
                                System.out.println("accept:"+ i++);
                                key = iterator.next();
                                iterator.remove();
                                if (key.isAcceptable()) {
                                    System.out.println("isAcceptable!");
                                    protocol.handleAccept(key, "Hello Client,I get the message.", 1024);
                                }
                                if (key.isReadable()) {
                                    System.out.println("handleRead!");
                                    Object o = protocol.handleRead(key);
                                    scheduler.receiver(o);
                                }
                            }
                        }catch (Exception e) {
                            if(key !=null){
                                key.cancel();
                                if(key.channel() !=null)
                                    key.channel().close();
                            }
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
