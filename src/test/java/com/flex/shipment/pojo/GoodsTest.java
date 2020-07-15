package com.flex.shipment.pojo;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @Description:
 * @Author: flex
 * @Date: 18:33 2020/7/15
 */
public class GoodsTest {

    @Test
    public void test() throws IllegalAccessException, InstantiationException {
        Class<Goods> goodsClass = Goods.class;
        Goods goods = goodsClass.newInstance();
        System.out.println(goods.getTon());
    }
    


}
