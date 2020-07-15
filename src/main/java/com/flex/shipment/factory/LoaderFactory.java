package com.flex.shipment.factory;

import com.flex.shipment.action.Loader;
import com.flex.shipment.pojo.Shipment;

/**
 * @Description: LoaderFactory class
 * @Author: flex
 * @Date: 14:11 2020/7/14
 */
public class LoaderFactory {

    public <T> Loader<T> createLoaderByAll(Shipment<T> res, Shipment<T> des,Class<T> t){
        return new Loader<T>(res,des);
    }

    public <T> Loader<T> createLoaderByNum(Shipment<T> res, Shipment<T> des,int num,Class<T> t){
        return new Loader<T>(res,des,num);
    }

}
