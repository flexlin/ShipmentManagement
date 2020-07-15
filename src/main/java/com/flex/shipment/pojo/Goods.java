package com.flex.shipment.pojo;

import java.io.Serializable;

/**
 * @Description: The unit of Goods is ton.
 * @Author: flex
 * @Date: 12:18 2020/7/14
 */
public class Goods implements Serializable {
    private final int ton = 1;

    public int getTon() {
        return ton;
    }

}
