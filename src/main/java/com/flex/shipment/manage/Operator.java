package com.flex.shipment.manage;

import java.util.Map;

/**
 * @Description: A through S and D scheduler obtain B
 * @Author: flex
 * @Date: 23:41 2020/7/15
 */
public interface Operator<A,S,D,B> {

    Map<B, Boolean> scheduler(A a,S s,D d);

}
