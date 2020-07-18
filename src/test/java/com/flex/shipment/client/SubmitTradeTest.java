package com.flex.shipment.client;

import com.flex.shipment.enums.Status;
import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: flex
 * @Date: 0:38 2020/7/16
 */
public class SubmitTradeTest {

    public static void main(String[] args)throws Exception {
        long start = System.currentTimeMillis();
        int n = 0;
        int num = 500;
        ArrayList<Thread> threads = new ArrayList<>();
        while (n<20) {
            SendThread sendThread = new SendThread(n * num, num, 200);
            Thread thread = new Thread(sendThread);
            threads.add(thread);
            thread.start();
            thread.join();
            System.out.println(n);
            n++;
        }
        System.out.println("---------------------exec---------------------");

        long end = System.currentTimeMillis();
        System.out.println("time : "+(end - start)/1000 +"s");
    }

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

    @Test
    public void testN() throws Exception {
        long start = System.currentTimeMillis();
        int n = 0;
        SubmitTrade submitTrade = new SubmitTrade();
        while (n<10000){
            Trade<Goods> trade = new Trade<Goods>("myTrade-"+n, 100, Goods.class);
            try {
                submitTrade.submit(trade,"localhost",10230);
            }catch (Exception e){
                e.printStackTrace();
            }
            n++;
        }
        long end = System.currentTimeMillis();
        System.out.println("time : "+(end - start)/1000 +"s");
    }

    @Test
    public void testNWithThread()throws Exception{
        long start = System.currentTimeMillis();
        int n = 0;
        int num = 100;
        while (n<100) {
            SendThread sendThread = new SendThread(n * num, num, 200);
            Thread thread = new Thread(sendThread);
            thread.start();
            thread.join();
            n++;
        }
        System.out.println("---------------------exec---------------------");

        long end = System.currentTimeMillis();
        System.out.println("time : "+(end - start)/1000 +"s");
    }

}
