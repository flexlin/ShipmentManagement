package com.flex.shipment.manage;

import com.flex.shipment.pojo.Goods;
import com.flex.shipment.pojo.Trade;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Description:
 * @Author: flex
 * @Date: 18:26 2020/7/14
 */
public class ManagerBackendTest {

    @Test
    public void server(){
        new ManagerBackend().start();
    }

    @Test
    public void test() throws IOException {


    }



}
