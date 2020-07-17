package com.flex.shipment.manage;

import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: system scheduler
 * @Author: flex
 * @Date: 17:25 2020/7/14
 */
public class Scheduler {

    private SupplierListener supplierListener;
    private SupplierManager supplierManager;
    private TaskManager taskManager;
    private LinkedBlockingQueue<Object> objectsPool;

    public Scheduler(SupplierListener supplierListener,SupplierManager supplierManager, TaskManager taskManager){
        this.supplierListener = supplierListener;
        this.supplierManager = supplierManager;
        this.taskManager = taskManager;
        this.objectsPool = supplierManager.getObjectsPool();
    }

    public void receiver(Object o){
        try {
            objectsPool.put(o);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public Thread start(){
        return new Thread(){
            @Override
            public void run() {
                System.out.println("Scheduler starting...");
                LinkedBlockingQueue<SupplierEvent> suppliers = supplierListener.getSuppliers();
                while (true) {
                    try {
                        if (suppliers.isEmpty()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        } else {
                            SupplierEvent event = suppliers.poll();
                            Supplier supplier = event.getSupplier();
                            Status status = event.getStatus();
                            System.out.println("Scheduler status:" + status);
                            switch (status) {
                                case START:
                                    supplier.executePlan(); break;
                                case SPLIT:
                                    taskManager.createTask(event); break;
                                case MERGE:
                                    taskManager.createTask(event); break;
                                case CHANGE:
                                    taskManager.createTask(event); break;
                                case CONFIRM: {
                                    supplierManager.removeTrade(event.getTrade().getTradeId());
                                    taskManager.removeTrade(event.getTrade().getTradeId());
                                }; break;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }









}
