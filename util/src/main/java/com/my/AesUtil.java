package com.my;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Created by dell on 2018/3/15.
 */
public class AesUtil {
    private Cipher cipherEncode = null;
    private Cipher cipherDecode = null;

    public AesUtil() {
    }

    public void init(String strKey, String strIV) {
        if(strKey.length() < 16) {
            throw new RuntimeException("Key length must be at least 16");
        } else if(strIV.length() < 16) {
            throw new RuntimeException("IV length must be at least 16");
        } else {
            try {
                strKey = strKey.substring(0, 16);
                strIV = strIV.substring(0, 16);
                byte[] e = strKey.getBytes("utf-8");
                Security.addProvider(new BouncyCastleProvider());
                SecretKeySpec key = new SecretKeySpec(e, "AES");
                byte[] bysIV = strKey.getBytes("utf-8");
                IvParameterSpec iv = new IvParameterSpec(bysIV);
                this.cipherEncode = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
                this.cipherEncode.init(1, key, iv);
                this.cipherDecode = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
                this.cipherDecode.init(2, key, iv);
            } catch (Exception var7) {
                throw new RuntimeException(var7);
            }
        }
    }

    public byte[] encode(byte[] bysContent) {
        if(this.cipherEncode == null) {
            return bysContent;
        } else {
            try {
                byte[] e = (byte[])null;
                Cipher var3 = this.cipherEncode;
                synchronized(this.cipherEncode) {
                    e = this.cipherEncode.doFinal(bysContent);
                }

                return e;
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    public String encode(String strText) {
        if(this.cipherEncode == null) {
            return strText;
        } else {
            try {
                byte[] e = strText.getBytes("utf-8");
                byte[] bysResult = (byte[])null;
                Cipher strResult = this.cipherEncode;
                synchronized(this.cipherEncode) {
                    bysResult = this.cipherEncode.doFinal(e);
                }

                String strResult1 = Base64.encode(bysResult);
                return strResult1;
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }
    }

    public byte[] decode(byte[] bysEncoded) {
        if(this.cipherEncode == null) {
            return bysEncoded;
        } else {
            try {
                byte[] e = (byte[])null;
                Cipher var3 = this.cipherDecode;
                synchronized(this.cipherDecode) {
                    e = this.cipherDecode.doFinal(bysEncoded);
                }

                return e;
            } catch (Exception var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    public String decode(String strEncoded) {
        if(this.cipherEncode == null) {
            return strEncoded;
        } else {
            byte[] bysEncoded = Base64.decode(strEncoded);

            try {
                byte[] e = (byte[])null;
                Cipher strResult = this.cipherDecode;
                synchronized(this.cipherDecode) {
                    e = this.cipherDecode.doFinal(bysEncoded);
                }

                String strResult1 = new String(e, "utf-8");
                return strResult1;
            } catch (Exception var6) {
                throw new RuntimeException(var6);
            }
        }
    }

    public static void main(String[] args) {
//        DECSECURITYKEYABCDEFG|EiJPWIgQQDgoJXlRy91SZncpdZgwQEHi
        AesUtil test = new AesUtil();
        test.init("DECSECURITYKEYABCDEFG", "EiJPWIgQQDgoJXlRy91SZncpdZgwQEHi");
        String strEncoded = "DF8f6QH+RN6VHkKwWGg+Bg==";
//        System.out.println(test.encode("15010363333"));
        System.out.println(test.decode(strEncoded));
    }

    public static void decode(String message,String blank) {
//        DECSECURITYKEYABCDEFG|EiJPWIgQQDgoJXlRy91SZncpdZgwQEHi

        String strEncoded = "DF8f6QH+RN6VHkKwWGg+Bg==";
//        System.out.println(test.encode("15010363333"));

    }
}
