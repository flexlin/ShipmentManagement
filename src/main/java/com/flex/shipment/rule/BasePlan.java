package com.flex.shipment.rule;

import com.flex.shipment.enums.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 *              splits: the quantity of goods for rate;
 *              merge: the merge of shipment index
 * @Author: flex
 * @Date: 19:37 2020/7/14
 */
public class BasePlan implements Plan {
    //shipments of goods quantity
    private Integer[] split = {2,3,5};
    //shipment of index
    private Integer[] merge = {1,2};

    private List<Operator> opts;

    public BasePlan(){}

    public BasePlan(Integer[] split,Integer[] merge){
        this.split = split;
        this.merge = merge;
    }

    public int getSplitsSum(){
        int n = 0;
        for (int i=0;i<split.length;i++){
            n += split[i];
        }
        return n;
    }

    public Integer[] splits() {
        return split;
    }

    public Integer[] merge() {
        return merge;
    }

    public List<Operator> getOpts() {
        if (opts == null){
            if(split != null){
                opts = new ArrayList<Operator>();
                opts.add(Operator.SPLIT);
                opts.add(Operator.MERGE);
            }
        }
        return opts;
    }

    public boolean isGrow() {
        for (int i=0;i<split.length-1;i++){
            if(split[i]>split[i+1]) return false;
        }
        return true;
    }

    public Integer[] getSplits() {
        return split;
    }

    public void setSplits(Integer[] splits) {
        this.split = splits;
    }

    public Integer[] getMerge() {
        return merge;
    }

    public void setMerge(Integer[] merge) {
        this.merge = merge;
    }


}
