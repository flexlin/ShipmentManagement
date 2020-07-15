package com.flex.shipment.factory;

import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Shipment;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: flex
 * @Date: 15:03 2020/7/14
 */
public class ShipmentFactoryTest {
    @Test
    public void test(){
        Shipment<Goods> shipment = ShipmentFactory.createShipment(100, Goods.class);

    }
}
