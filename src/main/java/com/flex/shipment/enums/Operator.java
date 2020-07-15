package com.flex.shipment.enums;

/**
 * @Description:
 * @Author: flex
 * @Date: 0:44 2020/7/15
 */
public enum Operator {

    SPLIT(1,"split"),
    MERGE(2,"merge"),
    CHANGE(3,"change");

    private int code;
    private String name;

    Operator(int code, String name){
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
