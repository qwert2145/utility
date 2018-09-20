package com.my;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64 {
    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

    public Base64() {
    }

    public static String encode(byte[] bysData) {
        String strEncoded = null;
        BASE64Encoder var2 = encoder;
        synchronized(encoder) {
            strEncoded = encoder.encode(bysData);
        }

        strEncoded = strEncoded.replaceAll("\r\n", "");
        return strEncoded;
    }

    public static byte[] decode(String strData) {
        try {
            BASE64Decoder e = decoder;
            synchronized(decoder) {
                return decoder.decodeBuffer(strData);
            }
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }
}