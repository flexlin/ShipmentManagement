package com.flex.shipment.client;

import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;

/**
 * @Description:
 * @Author: flex
 * @Date: 14:54 2020/7/17
 */
public class SendThread implements Runnable {

    private int start;
    private int num;
    private int total;

    public SendThread(int start,int num,int total){
        this.start = start;
        this.num = num;
        this.total = total;
    }

    @Override
    public void run() {
        try {
            int n = start;
            SubmitTrade submitTrade = new SubmitTrade();
            System.out.println("thread num: "+num);
            while (n < start + num) {
                Trade<Goods> trade = new Trade<Goods>("myTrade-" + n, total, Goods.class);
                try {
                    submitTrade.submit(trade, "localhost", 10230);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                n++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
