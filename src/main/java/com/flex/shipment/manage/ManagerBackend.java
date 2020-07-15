package com.flex.shipment.manage;

import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//    private ExecutorService threadPool = Executors.newCachedThreadPool();


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
                    ServerSocket server = new ServerSocket(10230);
                    while (true) {
                        Socket socket = server.accept();
                        InputStream inputStream = socket.getInputStream();

                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        try {
                            Trade<Goods> trade = (Trade<Goods>) objectInputStream.readObject();

                            scheduler.receiver(trade);

                            System.out.println("goodsTrade num: " + trade.getTotal().getNum());

                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

                        inputStream.close();
                        outputStream.close();
                        socket.close();
                    }
//            server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }


}
