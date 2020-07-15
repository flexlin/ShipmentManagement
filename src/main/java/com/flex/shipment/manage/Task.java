package com.flex.shipment.manage;

import com.flex.shipment.action.Loader;
import com.flex.shipment.enums.Status;
import com.flex.shipment.pojo.Shipment;
import com.flex.shipment.pojo.Trade;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @Description: this class is the task for operator
 * @Author: flex
 * @Date: 12:47 2020/7/14
 */
public class Task<T> {

    private String taskId;
    private Status status;
    private Supplier<T> supplier;
    private Trade<T> trade;
    private Operator operator;
    private Map<Loader<T>,Boolean> lists;
    private Boolean finished = false;

    public Task(Status status, Supplier<T> supplier, Trade<T> trade, Operator operator){
        this.status = status;
        this.trade = trade;
        this.supplier = supplier;
        this.operator = operator;
        this.lists = operator.scheduler(supplier,status,trade);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Boolean getFinished() {
        if (lists != null && lists.keySet().size()>0) {
            Boolean n = true;
            for (Loader<T> l : lists.keySet()) {
                n &= lists.get(l);
            }
            finished = n;
        }
        return finished;
    }

    public void runTask(ExecutorService pool){
        try {
            System.out.println("size:"+lists.size());
            for(Loader<T> l: lists.keySet()){
                Future<Boolean> submit = pool.submit(l);
                Boolean bool = submit.get();
                lists.put(l,bool);
            }
//            pool.awaitTermination(5,TimeUnit.SECONDS);
            while (!getFinished()){
                Thread.sleep(1000);
                System.out.println("task waiting!");
            }
            System.out.println("task finished!");

            List<Shipment<T>> split = supplier.split();
            for (int i=0;i<split.size();i++){
                System.out.println("Shipment "+i+":"+split.get(i).getAddr().size());
            }
            Shipment<T> merge = supplier.merge();
            System.out.println("Shipment merge:"+merge.getAddr().size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
