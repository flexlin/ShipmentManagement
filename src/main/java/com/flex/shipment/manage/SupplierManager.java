package com.flex.shipment.manage;

import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.ShipmentFactory;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;
import com.flex.shipment.pojo.Tuple;
import com.flex.shipment.rule.BasePlan;

import java.util.HashMap;
import java.util.Map;

import static jdk.nashorn.internal.objects.NativeMath.random;

/**
 * @Description: This class management Supplier
 * @Author: flex
 * @Date: 21:03 2020/7/14
 */
public class SupplierManager {
    // key is trade name
    private Map<String, Tuple<Supplier, Trade>> map = new HashMap<String, Tuple<Supplier, Trade>>();
    private SupplierListener supplierListener;
    private GoodsFactory goodsFactory;

    public SupplierManager(GoodsFactory goodsFactory,SupplierListener supplierListener){
        this.goodsFactory = goodsFactory;
        this.supplierListener = supplierListener;
    }

    public Tuple<Supplier, Trade> getSupplier(String name){
        Tuple<Supplier, Trade> tuple = map.get(name);
        return tuple;
    }

    public Supplier createSupplier(Trade trade){
        if(map.get(trade.getName()) == null) {
            BasePlan basePlan = new BasePlan();
            String name = "supplier" + System.currentTimeMillis() + random(1000);
            Supplier<Goods> supplier = new Supplier<Goods>(name, trade, goodsFactory, basePlan, supplierListener);
            supplier.setStatus(Status.START);
            this.map.put(trade.getName(), new Tuple<Supplier, Trade>(supplier, trade));
            return supplier;
        }else {
            Tuple<Supplier, Trade> tuple = map.get(trade.getName());
            SupplierEvent<Goods> event = new SupplierEvent<Goods>(trade.getStatus(), tuple.getA(), trade);
            supplierListener.register(event);
        }
        return null;
    }




}