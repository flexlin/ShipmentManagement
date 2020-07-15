package com.flex.shipment.manage;

import com.flex.shipment.enums.Status;
import com.flex.shipment.events.SupplierEvent;
import com.flex.shipment.pojo.Trade;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: management Task
 * @Author: flex
 * @Date: 19:52 2020/7/14
 */
public class TaskManager {
    // this key is taskId
    private Map<String, Task> map = new HashMap<String, Task>();
    // this key is tradeId
    private Map<String, List<Task>> tradeMapTask = new HashMap<String, List<Task>>();
    private Operator operator;
    private LinkedBlockingQueue<Task> taskPool = new  LinkedBlockingQueue<Task>();
    private ExecutorService threadPool = Executors.newFixedThreadPool(50);

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public TaskManager(Operator operator){
        this.operator = operator;
    }

    public <T> Task<T> createTask(SupplierEvent<T> event){
        Trade<T> trade = event.getTrade();
        Supplier<T> supplier = event.getSupplier();
        Status status = event.getStatus();
        Task<T> task = new Task<T>(status, supplier, trade, operator);
        task.setTaskId(trade.getTradeId()+"&"+"task"+System.currentTimeMillis()+ new Random(1000).nextInt());
        System.out.println("TaskManager TaskId:"+task.getTaskId()+","+status);
        map.put(task.getTaskId(),task);
        List<Task> tasks = null;
        if(tradeMapTask.get(trade.getTradeId()) == null) {
            tasks = new ArrayList<Task>();
        }else {
            tasks = tradeMapTask.get(trade.getTradeId());
        }
        tasks.add(task);
        tradeMapTask.put(trade.getTradeId(),tasks);
        taskPool.add(task);
        return task;
    }

    public Thread start(){
        return new Thread() {
            @Override
            public void run() {
                System.out.println("TaskManager starting!");
                while (true) {
                    try {
                        Task poll = taskPool.poll();
                        if (poll != null) {
                            executeTask(poll);
                            while (!poll.getFinished()) {
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void executeTask(Task task){
        task.runTask(threadPool);
    }



}
