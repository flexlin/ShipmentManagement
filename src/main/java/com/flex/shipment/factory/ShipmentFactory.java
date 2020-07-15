package com.flex.shipment.factory;

import com.flex.shipment.pojo.Shipment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: flex
 * @Date: 13:51 2020/7/14
 */
public class ShipmentFactory {

    public static <T> Shipment<T> createShipment(int quantity,Class<T> t){
        return new Shipment<T>(quantity);
    }


    public static <T> List<Shipment<T>> buildPlanShipments(int[] quantities,Class<T> t){
        List<Shipment<T>> shipments = new ArrayList<Shipment<T>>();
        for(int i=0;i<quantities.length;i++){
            shipments.add(new Shipment<T>(quantities[i]));
        }
        return shipments;
    }

}
