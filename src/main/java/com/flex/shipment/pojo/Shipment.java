package com.flex.shipment.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Description: this is a box for ship goods.
 * @Author: flex
 * @Date: 11:54 2020/7/14
 */
public class Shipment<T> implements Serializable {
    // the quantity
    private int num;
    private ArrayBlockingQueue<T> addr;
    private List<Shipment<T>> deps;
    private List<Shipment<T>> child;

    public Shipment(int num){
        this.num = num;
        this.addr = new ArrayBlockingQueue<T>(num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public ArrayBlockingQueue<T> getAddr() {
        return addr;
    }

    public void setAddr(ArrayBlockingQueue<T> addr) {
        this.addr = addr;
    }

    public List<Shipment<T>> getDeps() {
        return deps;
    }

    public void setDeps(List<Shipment<T>> deps) {
        this.deps = deps;
    }

    public List<Shipment<T>> getChild() {
        return child;
    }

    public void setChild(List<Shipment<T>> child) {
        this.child = child;
    }
}
