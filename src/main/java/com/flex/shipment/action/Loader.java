package com.flex.shipment.action;

import com.flex.shipment.pojo.Shipment;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description:
 * @Author: flex
 * @Date: 12:49 2020/7/14
 */
public class Loader<T> implements Runnable{

    private Shipment<T> res;
    private Shipment<T> des;
    private Integer num;

    public Loader(Shipment<T> res,Shipment<T> des){
        this.res = res;
        this.des = des;
    }

    public Loader(Shipment<T> res,Shipment<T> des,int num){
        this.res = res;
        this.des = des;
        this.num = num;
    }

    public void run() {
        int n = 0;
        while (true) {
            if (num == null) {
                ArrayBlockingQueue<T> resAddr = res.getAddr();
                ArrayBlockingQueue<T> desAddr = des.getAddr();
                T poll = resAddr.poll();
                if (poll == null)break;
                desAddr.add(poll);
            }else {
                ArrayBlockingQueue<T> resAddr = res.getAddr();
                ArrayBlockingQueue<T> desAddr = des.getAddr();
                T poll = resAddr.poll();
                if (num == n)break;
                desAddr.add(poll);
                n++;
            }
        }
    }

}
