package com.flex.shipment.manage;

import com.flex.shipment.enums.Operator;
import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.pojo.Shipment;
import com.flex.shipment.pojo.Trade;
import com.flex.shipment.rule.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description:
 * @Author: flex
 * @Date: 11:43 2020/7/14
 */
public class Supplier<T> implements Baser<T>{

    private String name;
    private Plan plan;
    private Trade<T> trade;
    private Status status;
    private SupplierListener supplierListener;
    private GoodsFactory goodsFactory;
    private Integer loadSum;

    public Supplier(String name, Trade<T> trade, GoodsFactory goodsFactory, Plan plan, SupplierListener supplierListener){
        this.name = name;
        this.trade = trade;
        this.plan = plan;
        this.goodsFactory = goodsFactory;
        this.supplierListener = supplierListener;
        this.loadSum = trade.getTotal().getNum();
    }

    public Integer getLoadSum() {
        return loadSum;
    }

    public void setLoadSum(Integer loadSum) {
        this.loadSum = loadSum;
    }

    public GoodsFactory getGoodsFactory() {
        return goodsFactory;
    }

    public void setGoodsFactory(GoodsFactory goodsFactory) {
        this.goodsFactory = goodsFactory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Trade<T> getTrade() {
        return trade;
    }

    public void setTrade(Trade<T> trade) {
        this.trade = trade;
    }

    public List<Shipment<T>> split() {
        Shipment<T> shipment = trade.getTotal();
        if(shipment.getChild() == null) {
            Integer[] splits = plan.splits();
            List<Shipment<T>> tShipment = new ArrayList<Shipment<T>>(splits.length);
            for (int i=0;i<splits.length;i++) {
                Shipment<T> tShipment1 = new Shipment<T>(splits[i] * shipment.getNum() / this.plan.getSplitsSum());
                ArrayList<Shipment<T>> arrayList = new ArrayList<Shipment<T>>();
                arrayList.add(shipment);
                tShipment1.setDeps(arrayList);
                tShipment.add(tShipment1);
            }
            shipment.setChild(tShipment);
            return tShipment;
        }else {
            return shipment.getChild();
        }
    }

    public Shipment<T> merge() {
        int n = 0;
        List<Shipment<T>> split = this.split();
        Integer[] merge = this.plan.merge();
        if(split.get(merge[0]).getChild()==null) {
            ArrayList<Shipment<T>> shipments = new ArrayList<Shipment<T>>();
            for (Integer i : merge) {
                Shipment<T> tShipment = split.get(i);
                shipments.add(tShipment);
                n += tShipment.getNum();
            }
            Shipment<T> tShipment = new Shipment<T>(n);
            tShipment.setDeps(shipments);
            for (Integer i : merge) {
                Shipment<T> tShipment1 = split.get(i);
                ArrayList<Shipment<T>> shipments1 = new ArrayList<Shipment<T>>();
                shipments1.add(tShipment);
                tShipment1.setChild(shipments1);
            }
            return tShipment;
        }else {
            return split.get(merge[0]).getChild().get(0);
        }
    }

    public Shipment<T> change(int rootQuantity) {
        Shipment<T> tShipment = new Shipment<T>(rootQuantity);
        trade.setTotal(tShipment);
        this.split();
        this.merge();
        return tShipment;
    }

    /**
     * obtain non-empty Shipment
     * @param shipment
     * @param shipments
     * @return
     */
    public List<Shipment<T>> getNonEmptyShipments(Shipment<T> shipment,List<Shipment<T>> shipments){
        ArrayBlockingQueue<T> addr = shipment.getAddr();
        if (!addr.isEmpty()){
            shipments.add(shipment);
        }else {
            List<Shipment<T>> child = shipment.getChild();
            if (child != null) {
                for (Shipment<T> s : child) {
                    getNonEmptyShipments(s, shipments);
                }
            }
        }
        return shipments;
    }

    /**
     * obtain leaf Shipment
     * @param shipment
     * @param shipments
     * @return
     */
    public List<Shipment<T>> getLeafShipments(Shipment<T> shipment,List<Shipment<T>> shipments){
        List<Shipment<T>> child = shipment.getChild();
        if (child == null) {
            shipments.add(shipment);
        }else {
            for (Shipment<T> s : child) {
                getLeafShipments(s, shipments);
            }
        }
        return shipments;
    }

    /**
     * execute plan
     */
    public void executePlan(){
        List<Operator> opts = plan.getOpts();
        for(Operator o:opts){
            if (Operator.SPLIT.getName().equals(o.getName())){
                SupplierEvent<T> event = new SupplierEvent<T>(Status.SPLIT,this,trade);
                supplierListener.register(event);
            }else if (Operator.MERGE.getName().equals(o.getName())){
                SupplierEvent<T> event = new SupplierEvent<T>(Status.MERGE, this,trade);
                supplierListener.register(event);
            }
        }
    }


}
