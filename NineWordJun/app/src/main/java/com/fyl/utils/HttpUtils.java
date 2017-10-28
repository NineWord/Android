package com.fyl.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * http 操作翻转类
 * Created by fyl on 2016/5/17.
 */
public class HttpUtils {
    private static String TAG = "HttpUtils";


    private static String sDisposition = "Content-Disposition: form-data;";
    private static String sBoundary = "----WebKitFormBoundary6FvcGEDbYY6p5tMK";
    private static String sTwoHyphens = "--";
    private static String sNewline = "\r\n";
    private static String sStartBoundary = sTwoHyphens + sBoundary + sNewline;
    private static String sEndBoundary = sTwoHyphens + sBoundary + sTwoHyphens + sNewline;

    /**
     * 获取http 包体文件名附带的参数
     */
    private static String getFileParam(String paramName, String fileName) {
        return
                sStartBoundary +
                sDisposition +
                " name=\"" +
                paramName +
                "\"; " + "filename=\"" +
                fileName +
                "\"; " +
                sNewline;
    }

    /**
     * 获取http 包体附带的参数
     */
    private static String getParam(String paramName, String paramValue) {
        return
                sStartBoundary +
                sDisposition +
                " name=\"" +
                paramName +
                "\"" +
                sNewline +
                sNewline +
                paramValue +
                sNewline;
    }

    /**
     * post文件
     *
     * @param paramMap      附带参数如："uuid=设备唯一标识","ver=1.2.0.2"
     * @param fileParamName 指定上传文件名称参数
     * @param filePath      上传文件路径
     * @return 返回结果
     */
    public static String postFile(String urlString, Map<String, String> paramMap,
                                  String fileParamName, String filePath) {

        if (StringUtils.isEmpty(urlString) || StringUtils.isEmpty(fileParamName) ||
                StringUtils.isEmpty(filePath)) {
            Log.e(TAG, "postFile parameter error");
            return "";
        }

        StringBuilder stringBuffer = new StringBuilder();
        DataOutputStream dos = null;
        FileInputStream fis = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                    + sBoundary);
            dos = new DataOutputStream(httpURLConnection.getOutputStream());
            fis = new FileInputStream(filePath);

            if (paramMap != null) {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    dos.writeBytes(getParam(entry.getKey(), entry.getValue()));
                }
            }
            dos.writeBytes(getFileParam(fileParamName, new File(filePath).getName()));
            // dos.writeBytes("Content-Type: image/png" + end);
            dos.writeBytes(sNewline);

            byte[] buffer = new byte[1024];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            dos.writeBytes(sNewline + sEndBoundary);
            dos.flush();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    stringBuffer.append(sNewline);
                }

            }
        } catch (IOException e) {
            stringBuffer.append(e.toString());
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return stringBuffer.toString();
    }


    /**
     * 判断是否能连上url
     *
     * @return true连接成功，false失败
     * @throws IllegalArgumentException 参数错误
     */
    public static boolean isConnect(String urlString) {
        if (urlString == null || urlString.length() == 0) {
            Log.e(TAG, "param error");
            throw new IllegalArgumentException("param error");
        }
        boolean result = false;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3 * 1000);
            httpURLConnection.setReadTimeout(3 * 1000);
            httpURLConnection.setRequestMethod("GET");

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return result;
    }

    /**
     * 下载图片
     *
     * @return bitmap 图片
     */
    public static Bitmap getNetWorkBitmap(String urlString) {
        URL imgUrl;
        Bitmap bitmap = null;
        HttpURLConnection urlConn=null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
             urlConn = (HttpURLConnection) imgUrl
                    .openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (MalformedURLException e) {
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return bitmap;
    }

    /**
     * 下载图片
     *
     * @return isSucces 是否成功
     */
    public static boolean getNetWorkImageFile(String urlString,File file) {
        URL imgUrl;
        boolean isSuccess=false;
        HttpURLConnection urlConn=null;
        try {
            imgUrl = new URL(urlString);
            // 使用HttpURLConnection打开连接
             urlConn = (HttpURLConnection) imgUrl
                    .openConnection();
            urlConn.setDoInput(true);
            urlConn.connect();
            // 将得到的数据转化成InputStream
            InputStream is = urlConn.getInputStream();
            // 将InputStream转换成Bitmap
            isSuccess=FileUtils.writeFile(file,is,false);
            is.close();
        } catch (MalformedURLException e) {
            System.out.println("[getNetWorkBitmap->]MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[getNetWorkBitmap->]IOException");
            e.printStackTrace();
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
        }
        return isSuccess;
    }



    public static class HttpResult {
        public boolean isSuccess;
        public String successString;
        public String errorString;

    }
}
