package com.flex.shipment.manage;

import org.junit.jupiter.api.Test;

import java.io.IOException;

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

    public static void main(String[] args) {
        new ManagerBackend().start();
    }


}
