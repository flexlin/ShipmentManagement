package com.flex.shipment.client;

import com.flex.shipment.enums.Status;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: flex
 * @Date: 0:38 2020/7/16
 */
public class SubmitTradeTest {

    @Test
    public void testStart() throws Exception {
        SubmitTrade submitTrade = new SubmitTrade();
        Trade<Goods> trade = new Trade<Goods>("myTrade", 100, Goods.class);
        submitTrade.submit(trade,"localhost",10230);
    }

    @Test
    public void testCHANGE1() throws Exception {
        SubmitTrade submitTrade = new SubmitTrade();
        Trade<Goods> trade = new Trade<Goods>("myTrade", 200, Goods.class);
        trade.setStatus(Status.CHANGE);
        submitTrade.submit(trade,"localhost",10230);
    }

    @Test
    public void testCHANGE2() throws Exception {
        SubmitTrade submitTrade = new SubmitTrade();
        Trade<Goods> trade = new Trade<Goods>("myTrade", 50, Goods.class);
        trade.setStatus(Status.CHANGE);
        submitTrade.submit(trade,"localhost",10230);
    }

}
