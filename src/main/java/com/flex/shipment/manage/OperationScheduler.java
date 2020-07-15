package com.flex.shipment.manage;

import com.flex.shipment.enums.Operator;
import com.flex.shipment.pojo.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: flex
 * @Date: 1:44 2020/7/15
 */
public class OperationScheduler {

    private Map<Supplier, Tuple<Operator,Task>> tasks = new HashMap<Supplier, Tuple<Operator, Task>>();


    public OperationScheduler(){

    }


}
