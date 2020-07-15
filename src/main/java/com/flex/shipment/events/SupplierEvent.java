package com.flex.shipment.events;

import com.flex.shipment.enums.Status;
import com.flex.shipment.manage.Supplier;
import com.flex.shipment.pojo.Trade;

/**
 * @Description:
 * @Author: flex
 * @Date: 17:17 2020/7/14
 */
public class SupplierEvent<T> {

    private Status status;
    private Trade<T> trade;
    private Supplier<T> supplier;

    public SupplierEvent(Status status, Supplier<T> supplier, Trade<T> trade){
        this.status = status;
        this.supplier = supplier;
        this.trade = trade;
    }

    public Trade<T> getTrade() {
        return trade;
    }

    public void setTrade(Trade<T> trade) {
        this.trade = trade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }
}
