package com.fyl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageUtils {

    private static final String Tag = "ImageUtils";

    public static Bitmap getBitmap(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    /**
     * 通过bitmap获取byte[]
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }


    /*
    *通过一个uri获取一个Bitmap对象
     *  */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // 读取uri所在的图片
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从服务器取图片
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (myFileUrl == null) {
            return null;
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSimpleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSimpleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSimpleSize;
    }

    public static Bitmap getSamllBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        options.inJustDecodeBounds = false;

        Bitmap bmp = null;
        try {
            int degrees = readPictureDegree(filePath);
            Matrix matrix = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap != bmp) {
                bitmap.recycle();
                System.gc();
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        if (path == null) return 0;
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static String bitmapToString(String filePath) {
        Bitmap bitmap = getSamllBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

//    public static String getSmallImage(String filePath) {
//        if (filePath.contains(FilePathUtils.TAKE_PHOTO_PATH + "thumb-")) return filePath;
//
//        File f = new File(filePath);
//        String savePath = FilePathUtils.TAKE_PHOTO_PATH + "thumb-" + System.currentTimeMillis() + f.getName();
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(savePath);
//            Bitmap bitmap = getSamllBitmap(filePath);
//            if (bitmap == null) {
//                savePath = filePath;
//            } else {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
//                fos.flush();
//                bitmap.recycle();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            savePath = filePath;
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return savePath;
//    }

    public static Bitmap rotateBitmapByDegress(Bitmap bitmap, int degree) {
        Bitmap returnBitmap = null;

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            returnBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            Log.e(Tag, "", e);
        }
        if (returnBitmap == null) {
            returnBitmap = bitmap;
        }

        if (bitmap != returnBitmap) {
            bitmap.recycle();
        }

        return returnBitmap;
    }

    public static int getBitmapDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Rect getImageSize(String filePath) {
        Rect rect = new Rect();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        rect.set(0, 0, options.outWidth, options.outHeight);
        return rect;
    }
}
