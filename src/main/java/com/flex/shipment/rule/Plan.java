package com.flex.shipment.rule;

import com.flex.shipment.enums.Operator;

import java.util.List;

/**
 * @Description: rule of supplier
 * @Author: flex
 * @Date: 14:13 2020/7/14
 */
public interface Plan {
    //shipments of goods quantity
    Integer[] splits(); //{2,3,5};
    //shipment of index
    Integer[] merge(); //{1,2};

    List<Operator> getOpts();

    boolean isGrow();

    int getSplitsSum();

}
