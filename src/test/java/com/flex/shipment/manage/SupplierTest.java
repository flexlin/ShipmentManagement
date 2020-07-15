package com.flex.shipment.manage;

import com.flex.shipment.events.SupplierListener;
import com.flex.shipment.factory.GoodsFactory;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Shipment;
import com.flex.shipment.pojo.Trade;
import com.flex.shipment.rule.BasePlan;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: flex
 * @Date: 17:45 2020/7/15
 */
public class SupplierTest {

    private Supplier<Goods> newInstance(){
        BasePlan basePlan = new BasePlan();
        Trade<Goods> trade = new Trade<Goods>("Trade" + System.currentTimeMillis(), 10,Goods.class);
        SupplierListener supplierListener = new SupplierListener();
        GoodsFactory goodsFactory = new GoodsFactory();
        Supplier<Goods> supplier = new Supplier<Goods>("Supplier" + System.currentTimeMillis(), trade, goodsFactory, basePlan, supplierListener);
        return supplier;
    }

    @Test
    public void testsplit() {
        Supplier<Goods> supplier = newInstance();
        List<Shipment<Goods>> split = supplier.split();
    }

    @Test
    public void testmerge() {
        Supplier<Goods> supplier = newInstance();

    }

    @Test
    public void testchange() {
        Supplier<Goods> supplier = newInstance();

    }

    @Test
    public void testgetLeafShipments() {
        Supplier<Goods> supplier = newInstance();
        ArrayList<Shipment<Goods>> shipments = new ArrayList<Shipment<Goods>>();
        List<Shipment<Goods>> list = supplier.getLeafShipments(supplier.getTrade().getTotal(), shipments);
        System.out.println(list.size());
    }

    @Test
    public void testNon(){
        Supplier<Goods> supplier = newInstance();
        ArrayList<Shipment<Goods>> shipments = new ArrayList<Shipment<Goods>>();
        List<Shipment<Goods>> list = supplier.getNonEmptyShipments(supplier.getTrade().getTotal(), shipments);
        System.out.println(list.size());
    }


}
