package com.flex.shipment.events;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description:
 * @Author: flex
 * @Date: 14:10 2020/7/14
 */
public class SupplierListener {
    private static LinkedBlockingQueue<SupplierEvent> suppliers = new LinkedBlockingQueue<SupplierEvent>();

    public void register(SupplierEvent event){
        System.out.println("SupplierListener TradeId:"+event.getTrade().getTradeId()+","+event.getStatus());
        suppliers.add(event);
    }

    public LinkedBlockingQueue<SupplierEvent> getSuppliers() {
        return suppliers;
    }
}
