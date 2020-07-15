package com.flex.shipment.manage;

import com.flex.shipment.action.Loader;
import com.flex.shipment.enums.Status;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.factory.LoaderFactory;
import com.flex.shipment.pojo.Shipment;
import com.flex.shipment.pojo.Trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: flex
 * @Date: 12:47 2020/7/14
 */
public class Task<T> {

    private Status status;
    private Supplier<T> supplier;
    private Trade<T> trade;
    private LoaderFactory loaderFactory;
    private Map<Loader<T>,Boolean> lists;

    public Task(Status status, Supplier<T> supplier, Trade<T> trade, LoaderFactory loaderFactory){
        this.status = status;
        this.trade = trade;
        this.supplier = supplier;
        this.loaderFactory = loaderFactory;
        generateTask();
    }

    public void generateTask(){
        this.lists = new HashMap<Loader<T>, Boolean>();
        switch (status){
            case SPLIT:{
                /**
                 * obtain Goods
                 */
                GoodsFactory goodsFactory = supplier.getGoodsFactory();
                Trade<T> trade = supplier.getTrade();
                goodsFactory.obtain(trade.getTotal(),trade.getTotal().getNum(), trade.getType());

                List<Shipment<T>> split = supplier.split();
                for(Shipment<T> s:split) {
                    Loader<T> loader = loaderFactory.createLoaderByAll(trade.getTotal(), s, trade.getType());
                    lists.put(loader,false);
                }
            };
            case MERGE:{
                List<Shipment<T>> child = supplier.getTrade().getTotal().getChild();
                Shipment<T> merge = supplier.merge();
                for (Shipment<T> c:child){
                    Loader<T> loader = loaderFactory.createLoaderByAll(c, merge, supplier.getTrade().getType());
                    lists.put(loader,false);
                }
            };
            case CHANGE:{
                int newNum = trade.getTotal().getNum();
                int oldNum = supplier.getTrade().getTotal().getNum();
                ArrayList<Shipment<T>> shipments = new ArrayList<Shipment<T>>();
                List<Shipment<T>> leafShipments = supplier.getLeafShipments(supplier.getTrade().getTotal(), shipments);
                supplier.setTrade(trade);
                supplier.change(newNum);

                if(newNum>oldNum){
                    /**
                     * obtain Goods
                     */
                    GoodsFactory goodsFactory = supplier.getGoodsFactory();
                    Trade<T> trade = supplier.getTrade();
                    int n = this.trade.getTotal().getNum() - trade.getTotal().getNum();
                    goodsFactory.obtain(trade.getTotal(), n, trade.getType());

                }else {
                    Integer[] splits = supplier.getPlan().splits();
                    Integer[] merge = supplier.getPlan().merge();
                    Integer[] total = new Integer[splits.length - merge.length + 1];
                    int k = 0;
                    int j = 0 ;
                    for(int i = 0; i<total.length; i++){
                        if(j<merge.length && k==merge[j]){
                            int x = 0;
                            for(; j<merge.length; j++){
                                x += splits[merge[j]];
                                k++;
                            }
                            total[i] = x;
                        }else {
                            total[i] = splits[k++];
                        }
                    }



                }
            };
        }
    }


}
