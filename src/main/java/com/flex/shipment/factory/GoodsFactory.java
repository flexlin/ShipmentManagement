package com.flex.shipment.factory;

import com.flex.shipment.pojo.Shipment;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description: this factory use
 * @Author: flex
 * @Date: 11:45 2020/7/15
 */
public class GoodsFactory {

    /**
     * obtain goods
     * @param shipment
     * @param t
     * @param <T>
     * @return
     */
    public <T> Shipment<T> obtain(Shipment<T> shipment,int n,Class<T> t){
        ArrayBlockingQueue<T> addr = shipment.getAddr();
        while (n>0){
            try {
                T instance = t.newInstance();
                addr.add(instance);
                n--;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        shipment.setAddr(addr);
        return shipment;
    }

    /**
     * back goods to factory
     * @param shipment
     * @param t
     * @param <T>
     * @return
     */
    public <T> Shipment<T> back(Shipment<T> shipment,int n,Class<T> t){
        ArrayBlockingQueue<T> addr = shipment.getAddr();
        while (n>0){
            addr.poll();
            n--;
        }
        return shipment;
    }

}
