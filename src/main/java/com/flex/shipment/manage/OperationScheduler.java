package com.flex.shipment.manage;

import com.flex.shipment.action.Loader;
import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import com.flex.shipment.pojo.Shipment;
import com.flex.shipment.pojo.Trade;
import com.flex.shipment.rule.Plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: scheduler operate
 * @Author: flex
 * @Date: 1:44 2020/7/15
 */
public class OperationScheduler<T> implements Operator<Supplier<T>,Status,Trade<T>,Loader<T>>{

    private LoaderFactory loaderFactory;
    private SupplierListener supplierListener;

    public OperationScheduler(LoaderFactory loaderFactory, SupplierListener supplierListener){
        this.loaderFactory = loaderFactory;
        this.supplierListener = supplierListener;
    }

    public Map<Loader<T>, Boolean> scheduler(Supplier<T> supplier,Status status, Trade<T> tradeNew) {
        HashMap<Loader<T>, Boolean> map = new HashMap<Loader<T>, Boolean>();
        switch (status){
            case SPLIT:{
                /**
                 * obtain Goods
                 */
                GoodsFactory goodsFactory = supplier.getGoodsFactory();
                Trade<T> trade = supplier.getTrade();
                goodsFactory.obtain(trade.getTotal(),supplier.getLoadSum(), trade.getType());

                List<Shipment<T>> split = supplier.split();
                for(Shipment<T> s:split) {
                    Loader<T> loader = loaderFactory.createLoaderByNum(trade.getTotal(), s,s.getNum(), trade.getType());
                    map.put(loader,false);
                }
            };break;
            case MERGE:{
                List<Shipment<T>> child = supplier.getTrade().getTotal().getChild();
                Plan plan = supplier.getPlan();
                Integer[] mergePlan = plan.merge();
                Shipment<T> merge = supplier.merge();
                for (Integer c:mergePlan){
                    Loader<T> loader = loaderFactory.createLoaderByAll(child.get(c), merge, supplier.getTrade().getType());
                    map.put(loader,false);
                }
            };break;
            case CHANGE:{
                int newNum = tradeNew.getTotal().getNum();
                int oldNum = supplier.getTrade().getTotal().getNum();
                ArrayList<Shipment<T>> shipments = new ArrayList<Shipment<T>>();
                List<Shipment<T>> leafShipmentsOld = supplier.getNonEmptyShipments(supplier.getTrade().getTotal(), shipments);
                Trade<T> trade = supplier.getTrade();
                supplier.setTrade(tradeNew);
                supplier.change(newNum);

                if(newNum>oldNum){

                    int n = tradeNew.getTotal().getNum() - trade.getTotal().getNum();
                    supplier.setLoadSum(n);

                    SupplierEvent<T> event = new SupplierEvent<T>(Status.START, supplier, trade);
                    supplierListener.register(event);

                    List<Shipment<T>> shipmentsNew = new ArrayList<Shipment<T>>();
                    List<Shipment<T>> leafShipmentsNew = supplier.getLeafShipments(supplier.getTrade().getTotal(), shipmentsNew);
                    for(int i=0; i<leafShipmentsOld.size(); i++){
                        Loader<T> loader = loaderFactory.createLoaderByAll(leafShipmentsOld.get(i), leafShipmentsNew.get(i), trade.getType());
                        map.put(loader,false);
                    }

                }else {
                    List<Shipment<T>> shipmentsNew = new ArrayList<Shipment<T>>();
                    List<Shipment<T>> leafShipmentsNew = supplier.getLeafShipments(supplier.getTrade().getTotal(), shipmentsNew);
                    for(int i=0; i<leafShipmentsOld.size(); i++){
                        Shipment<T> tShipment = leafShipmentsNew.get(i);
                        Loader<T> loader = loaderFactory.createLoaderByNum(leafShipmentsOld.get(i), tShipment,
                                tShipment.getNum(),supplier.getTrade().getType());
                        map.put(loader,false);
                    }
                }
            };break;
        }
        return map;
    }

}
