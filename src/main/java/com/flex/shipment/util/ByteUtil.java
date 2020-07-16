package com.flex.shipment.util;

/**
 * @Description:
 * @Author: flex
 * @Date: 14:23 2020/7/16
 */
public class ByteUtil {

    /**
     * merger two bytes
     * @param byte_1
     * @param byte_2
     * @return
     */
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
