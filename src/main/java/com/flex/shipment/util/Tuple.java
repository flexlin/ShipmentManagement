package com.flex.shipment.util;

/**
 * @Description: data struct
 * @Author: flex
 * @Date: 21:31 2020/7/14
 */
public class Tuple<A,B> {

    private A a;
    private B b;

    public Tuple(A a,B b){
        this.a = a;
        this.b = b;
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
