package com.fyl.utils;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUploadUtils {

    public void doUpload(String urlstr, String path, UploadCallback callback) {
        Log.i("FileUploadUtils", "start upload file");
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*******";

        HttpURLConnection httpURLConnection = null;
        FileInputStream fis = null;
        DataOutputStream dos = null;
        try {
            URL url = new URL(urlstr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
                    + path
                    + "\""
                    + end);
            dos.writeBytes(end);
            if (callback != null) {
                callback.start(path);
            }
            fis = new FileInputStream(path);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            Log.i("FileUploadUtils", "end upload file");
//				int responseCode=httpURLConnection.getResponseCode();
//				if(responseCode==-1){
//					throw new IOException("Could not retrieve response code from HttpUrlConnection.");
//				}

            InputStream is = httpURLConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }

            String result = sb.toString();
            Log.i("FileUploadUtils", result);
            if (callback != null) {
                callback.end(path, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    }

    class UploadLogRunnable implements Runnable {

        private final String path;
        private final String mUrl;
        private final UploadCallback callback;

        public UploadLogRunnable(String url, String path, UploadCallback callback) {
            this.mUrl = url;
            this.path = path;
            this.callback = callback;
        }

        @Override
        public void run() {
           doUpload(mUrl, path, callback);
        }
    }

    public interface UploadCallback {
        void start(String fileName);

        void end(String fileName, String result);
    }
}
