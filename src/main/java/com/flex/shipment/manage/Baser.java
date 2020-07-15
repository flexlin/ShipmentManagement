package com.flex.shipment.manage;

import com.flex.shipment.pojo.Shipment;

import java.util.List;

/**
 * @Description:
 * @Author: flex
 * @Date: 12:07 2020/7/14
 */
public interface Baser<T> {

    List<Shipment<T>> split();

    Shipment<T> merge();

    Shipment<T> change(int rootQuantity);

}
