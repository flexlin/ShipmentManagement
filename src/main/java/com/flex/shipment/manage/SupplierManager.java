package com.flex.shipment.manage;

import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;
import com.flex.shipment.util.Tuple;
import com.flex.shipment.rule.BasePlan;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static jdk.nashorn.internal.objects.NativeMath.random;

/**
 * @Description: This class is management Supplier
 * @Author: flex
 * @Date: 21:03 2020/7/14
 */
public class SupplierManager {
    // key is tradeId
    private Map<String, Tuple<Supplier, Trade>> map = new HashMap<String, Tuple<Supplier, Trade>>();
    private SupplierListener supplierListener;
    private GoodsFactory goodsFactory;
    private OperationScheduler operation;
    private LinkedBlockingQueue<Object> objectsPool = new LinkedBlockingQueue<Object>();

    public SupplierManager(GoodsFactory goodsFactory, LoaderFactory loaderFactory, SupplierListener supplierListener){
        this.goodsFactory = goodsFactory;
        this.supplierListener = supplierListener;
        this.operation = new OperationScheduler(loaderFactory,supplierListener);
    }

    public LinkedBlockingQueue<Object> getObjectsPool() {
        return objectsPool;
    }

    public OperationScheduler getOperation() {
        return operation;
    }

    public void setOperation(OperationScheduler operation) {
        this.operation = operation;
    }

    public Tuple<Supplier, Trade> getSupplier(String name){
        Tuple<Supplier, Trade> tuple = map.get(name);
        return tuple;
    }

    public Thread start(){
        return new Thread(){
            @Override
            public void run() {
                System.out.println("SupplierManager starting...");
                while (true){
                    try {
                        Object obj = objectsPool.poll();
                        if (obj != null) {
                             dealObject(obj);
                        } else {
                            Thread.sleep(1000);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }



    private void dealObject(Object o){
        if (o == null) return;
        Trade t = (Trade)o;
        Class<?> type = t.getType();
        Trade trade = null;
        if (Goods.class.equals(type)) {
            trade = (Trade<Goods>) t;
        }
        this.createSupplier(trade);
    }


    public Supplier createSupplier(Trade trade){
        try {
            if (map.get(trade.getTradeId()) == null) {
                BasePlan basePlan = new BasePlan();
                String name = "supplier" + System.currentTimeMillis() + random(1000);
                System.out.println("createSupplier:" + name);
                Supplier<Goods> supplier = new Supplier<Goods>(name, trade, goodsFactory, basePlan, supplierListener);
                supplier.setStatus(Status.START);
                this.map.put(trade.getTradeId(), new Tuple<Supplier, Trade>(supplier, trade));
                SupplierEvent event = new SupplierEvent(Status.START, supplier, trade);
                supplierListener.register(event);
                return supplier;
            } else {
                if (trade.getStatus() == Status.START) return null;
                if (trade.getTotal().getNum() == map.get(trade.getTradeId()).getB().getTotal().getNum()) {
                    return null;
                } else {
                    Tuple<Supplier, Trade> tuple = map.get(trade.getTradeId());
                    SupplierEvent<Goods> event = new SupplierEvent<Goods>(trade.getStatus(), tuple.getA(), trade);
                    supplierListener.register(event);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void removeTrade(String tradeId){
        map.remove(tradeId);
    }




}
