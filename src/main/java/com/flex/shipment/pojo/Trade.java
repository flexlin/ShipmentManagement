package com.flex.shipment.pojo;

import com.flex.shipment.enums.Status;

import java.io.Serializable;

/**
 * @Description:
 * @Author: flex
 * @Date: 11:54 2020/7/14
 */
public class Trade<T> implements Serializable {

    private Shipment<T> total;
    private String tradeId;
    private Status status;
    private Class<T> type;

    public Trade(String name,int total,Class<T> t){
        this.tradeId = name;
        this.type = t;
        this.total = new Shipment<T>(total);
        this.status = Status.START;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public void change(int n){
        total.setNum(n);
        this.status = Status.CHANGE;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Shipment<T> getTotal() {
        return total;
    }

    public void setTotal(Shipment<T> total) {
        this.total = total;
    }
}
