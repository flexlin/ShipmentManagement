package com.flex.shipment.manage;

import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import com.flex.shipment.factory.ShipmentFactory;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:
 * @Author: flex
 * @Date: 13:31 2020/7/14
 */
public class ManagerBackend {

    private LoaderFactory loaderFactory = new LoaderFactory();
    private ShipmentFactory shipmentFactory = new ShipmentFactory();
    private SupplierListener supplierListener = new SupplierListener();
    private GoodsFactory goodsFactory = new GoodsFactory();
    private SupplierManager supplierManager = new SupplierManager(goodsFactory,supplierListener);
    private Scheduler scheduler = new Scheduler(supplierListener,supplierManager);





    public void start(){
        new Thread(){
            @Override
            public void run() {
                try {
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
