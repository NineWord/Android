package com.fyl.utils;


import android.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * 采用MD5加密
 * <p>Title: Module Information         </p>
 * <p>Description: Function Description </p>
 * <p>Copyright: Copyright (c) 2015     </p>
 * <p>Company: ND Co., Ltd.       </p>
 * <p>Create Time: 2015年7月9日           </p>
 *
 * @author xujs
 *         <p>Update Time:                      </p>
 *         <p>Updater:                          </p>
 *         <p>Update Comments:                  </p>
 */
public final class MD5Util {

    /**
     * 默认字符集：UTF-8
     */
    private static final String CHARSET_UTF8 = "UTF-8";


    private MD5Util() {
    }

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    /**
     * 从byte[]生成md5
     *
     * @param byteArray
     * @return
     */
    public static String bytes2MD5(byte[] byteArray) {
        if (null != byteArray && byteArray.length > 0) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
                byte[] md5Bytes = md5.digest(byteArray);
                StringBuffer hexValue = new StringBuffer();
                for (int i = 0; i < md5Bytes.length; i++) {
                    int val = ((int) md5Bytes[i]) & 0xff;
                    if (val < 16)
                        hexValue.append("0");
                    hexValue.append(Integer.toHexString(val));
                }
                return hexValue.toString();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    /**
     * MD5加码 生成32位md5码
     * <p>Description:              </p>
     * <p>Create Time: 2015年7月9日   </p>
     * <p>Create author: xujs   </p>
     *
     * @param inStr
     * @return
     *
     *
     *  ps:中文字符会出错
     * @see MD5Util#MD5Encoder(String)
     */
    @Deprecated
    public static String string2MD5(String inStr) {
        if (inStr != null && !("".equals(inStr))) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                android.util.Log.e("MD5", "string2MD5 MessageDigest.getInstance", e);
                return "";
            }
            char[] charArray = inStr.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for (int i = 0; i < charArray.length; i++)
                byteArray[i] = (byte) charArray[i];
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        }
        return "";
    }

    /**
     * MD5加码 生成32位md5码
     * <p>Description:              </p>
     * <p>Create Time: 2016-12-27 16:43:55   </p>
     *
     * @param s
     * @return
     */

    public final static String MD5Encoder(String s) {
        try {
            byte[] btInput = s.getBytes(CHARSET_UTF8);
            MessageDigest mdInst = null;
            try {
                mdInst = MessageDigest.getInstance("MD5");
            } catch (Exception e) {
                android.util.Log.e("MD5", "string2MD5 MessageDigest.getInstance", e);
                return "";
            }
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(val));
            }
            return sb.toString();
        } catch (Exception e) {
            android.util.Log.e("MD5", "MD5Util", e);
            return "";
        }
    }

    /**
     * 加密解密算法 执行一次加密，两次解密
     * <p>Description:              </p>
     * <p>Create Time: 2015年7月9日   </p>
     * <p>Create author: xujs   </p>
     *
     * @param inStr
     * @return
     */
    public static String convertMD5(String inStr) {
        if (inStr != null && !("".equals(inStr))) {
            char[] a = inStr.toCharArray();
            for (int i = 0; i < a.length; i++) {
                a[i] = (char) (a[i] ^ 't');
            }
            String s = new String(a);
            return s;
        }
        return "";
    }

    /**
     * 判断文件与已知的MD5是否相同是否相同MD5
     * <p>Description:              </p>
     * <p>Create Time: 2015年9月23日   </p>
     * <p>Create author: Administrator   </p>
     *
     * @param path1
     * @param MD5
     * @return
     */
    public static boolean checkIsSameMD5(String path1, String MD5) {
        try {
            return getFileMD5String(path1).equals(MD5);
        } catch (IOException e) {
            android.util.Log.e("MD5", "checkIsSameMD5", e);
            return false;
        }
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            android.util.Log.e("MD5", "getFileMD5String MessageDigest.getInstance", e);
            return "";
        }

        InputStream fis;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int numRead = 0;
        while ((numRead = fis.read(buffer)) > 0) {
            md5.update(buffer, 0, numRead);
        }
        fis.close();
        return bufferToHex(md5.digest());
    }

    public static String getFileMD5String(String path) throws IOException {
        File file = new File(path);
        return getFileMD5String(file);
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }


    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = HEX_DIGITS[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>> 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
        char c1 = HEX_DIGITS[bt & 0xf];// 取字节中低 4 位的数字转换
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
