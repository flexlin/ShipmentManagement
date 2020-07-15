package com.flex.shipment.manage;

import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: flex
 * @Date: 17:25 2020/7/14
 */
public class Scheduler {

    private SupplierListener supplierListener;
    private SupplierManager supplierManager;

    public Scheduler(SupplierListener supplierListener,SupplierManager supplierManager){
        this.supplierListener = supplierListener;
        this.supplierManager = supplierManager;
    }

    public void receiver(Trade trade){
        Supplier supplier = supplierManager.createSupplier(trade);
        if (supplier != null) {
            SupplierEvent<Goods> event = new SupplierEvent<Goods>(Status.START, supplier, trade);
            supplierListener.register(event);
        }
    }

    public void start(){
        new Thread(){
            @Override
            public void run() {
                LinkedBlockingQueue<SupplierEvent> suppliers = supplierListener.getSuppliers();
                while (true){
                    if (suppliers.isEmpty()){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        SupplierEvent event = suppliers.poll();
                        Supplier supplier = event.getSupplier();
                        Status status = event.getStatus();
                        switch (status){
                            case START: supplier.executePlan();
                            case SPLIT: supplier.split();
                            case MERGE:;
                            case CHANGE:;
                            case CONFIRM:;
                        }


                    }
                }
            }
        }.start();
    }









}
