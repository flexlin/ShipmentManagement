package com.flex.shipment.manage;

import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: flex
 * @Date: 0:53 2020/7/16
 */
public class TaskManagerTest{

    private static ExecutorService threadPool = Executors.newCachedThreadPool();
    @Test
    public void test() throws InterruptedException {
        GoodsFactory goodsFactory = new GoodsFactory();
        LoaderFactory loaderFactory = new LoaderFactory();
        SupplierListener supplierListener = new SupplierListener();
        SupplierManager supplierManager = new SupplierManager(goodsFactory,loaderFactory,supplierListener);
        TaskManager taskManager = new TaskManager(supplierManager.getOperation());
        threadPool.submit(taskManager.start());
//        threadPool.awaitTermination(10, TimeUnit.SECONDS);
    }


    public static void main(String[] args) {
        /*Thread thread = new Thread() {
            @Override
            public void run() {
                int i = 0;
                System.out.println("hello " + i++);
                while (true) {
                    System.out.println("hello " + i++);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("hello " + i++);
                }
            }
        };
        thread.start();*/

        GoodsFactory goodsFactory = new GoodsFactory();
        LoaderFactory loaderFactory = new LoaderFactory();
        SupplierListener supplierListener = new SupplierListener();
        SupplierManager supplierManager = new SupplierManager(goodsFactory,loaderFactory,supplierListener);
        TaskManager taskManager = new TaskManager(supplierManager.getOperation());
        threadPool.submit(taskManager.start());
        System.out.println("hello");
//        threadPool.awaitTermination(10, TimeUnit.SECONDS);

    }

}
