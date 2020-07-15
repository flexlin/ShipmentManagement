package com.flex.shipment.action;

import com.flex.shipment.pojo.Shipment;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * @Description:
 * @Author: flex
 * @Date: 12:49 2020/7/14
 */
public class Loader<T> implements Callable<Boolean> {

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

    public Boolean call() throws Exception {
        int n = 0;
        System.out.println("Loader:"+res.getAddr().size()+"-->"+des.getAddr().size());
        while (true) {
            try {
                if (num == null) {
                    ArrayBlockingQueue<T> resAddr = res.getAddr();
                    ArrayBlockingQueue<T> desAddr = des.getAddr();
                    if (n >= des.getNum() || resAddr.size() == 0 || desAddr.size() == des.getNum()) break;
                    T poll = resAddr.poll();

                    desAddr.add(poll);
                    n++;
                } else {
                    ArrayBlockingQueue<T> resAddr = res.getAddr();
                    ArrayBlockingQueue<T> desAddr = des.getAddr();
                    if (n >= des.getNum() || num <= n || resAddr.size() == 0 || desAddr.size() == des.getNum()) break;
                    T poll = resAddr.poll();
                    if (poll == null)break;
                    desAddr.add(poll);
                    n++;
                }
            }catch (Exception e){
                System.out.println("Loader error !");
                e.printStackTrace();
            }
        }
        return true;
    }

    public Shipment<T> getRes() {
        return res;
    }

    public void setRes(Shipment<T> res) {
        this.res = res;
    }

    public Shipment<T> getDes() {
        return des;
    }

    public void setDes(Shipment<T> des) {
        this.des = des;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
