package com.flex.shipment.util;

/**
 * @Description:
 * @Author: flex
 * @Date: 14:23 2020/7/16
 */
public class ByteUtil {

    /**
     * merger two bytes by length
     * @param byte_1
     * @param len_1
     * @param byte_2
     * @param len_2
     * @return
     */
    public static byte[] byteMergerByLength(byte[] byte_1, int len_1, byte[] byte_2, int len_2){
        byte[] byte_3 = new byte[len_1 + len_2];
        System.arraycopy(byte_1, 0, byte_3, 0, len_1);
        System.arraycopy(byte_2, 0, byte_3, len_1, len_2);
        return byte_3;
    }

    /**
     * merger two bytes
     * @param byte_1
     * @param byte_2
     * @return
     */
    public static byte[] byteMergerByAll(byte[] byte_1,byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
