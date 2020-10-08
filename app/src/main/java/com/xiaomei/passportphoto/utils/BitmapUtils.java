package com.xiaomei.passportphoto.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.xiaomei.passportphoto.logic.PhotoApp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.System.in;

public class BitmapUtils {

    public static String filename = "";
    public static String filename2 = "";
    public static String filename0 = "";
    static{
//        filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaomei/tmp";
//        filename2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaomei/filename";
//        filename0 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/xiaomei/0";
    }

    public static final String APP_ID = "22356022";
    public static final String API_KEY = "gAmhhkQ1aROQXkk3ol1DiWl0";
    public static final String SECRET_KEY = "Fo6QF8qByw6ww6cgUr2Q6CuP6yVZCYok";
    public static AipBodyAnalysis mClient = null;
    public static final String TAG = "xiaomei";
    public static AipBodyAnalysis getipBodyAnalysisInstance() {
        // 初始化一个AipBodyAna
        if (mClient != null) {
            return mClient;
        }
        AipBodyAnalysis client = new AipBodyAnalysis(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        return client;
    }

    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight)
    {
        if (bm == null)
        {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        float scale = (scaleHeight>scaleWidth)?scaleWidth:scaleHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        if (bm != null & !bm.isRecycled())
        {
            bm.recycle();
            bm = null;
        }
        return newbm;
    }

    public static Bitmap decodeBitmapFromByteArray(byte[] bytes){
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public static Bitmap changeBackground(Bitmap src, int color) {
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawColor(color);
        canvas.drawBitmap(src, 0, 0, null);
        return result;
    }

    public static void saveBitmap(Bitmap bitmap, String filepath){
        try {
            File file = new File(filepath);
            String dir = file.getParent();
            File dirAsFile = new File(dir);
            if(!dirAsFile.exists()) {
                dirAsFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fos);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveByteArray(byte[] bytes, String filepath){
        try {
            File file = new File(filepath);
            String dir = file.getParent();
            File dirAsFile = new File(dir);
            if(!dirAsFile.exists()) {
                dirAsFile.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte[] readFileToByteArray(String path) {
        File file = new File(path);
        if(!file.exists()) {
            return null;
        }
        try {
            FileInputStream in = new FileInputStream(file);
            long inSize = in.getChannel().size();//判断FileInputStream中是否有内容
            if (inSize == 0) {
                return null;
            }

            byte[] buffer = new byte[in.available()];//in.available() 表示要读取的文件中的数据长度
            in.read(buffer);  //将文件中的数据读到buffer中
            return buffer;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                return null;
            }
            //或IoUtils.closeQuietly(in);
        }
    }
    public static String saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
//        saveImageToGallery(bmp,picName);
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        String imagepath = galleryPath+picName + ".jpg";
        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(imagepath);
//            file = new File(galleryPath, photoName);
            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
        }catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(),file.getAbsolutePath(),fileName,null);
            MediaStore.Images.Media.insertImage(context.getContentResolver(),bmp,fileName,null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imagepath;
    }

    public static Bitmap horverImage(Bitmap bitmap, boolean H, boolean V) {
        int bmpWidth = bitmap.getWidth();
        int bmpHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();

        if (H)
            matrix.postScale(-1, 1);   //水平翻转H

        if (V)
            matrix.postScale(1, -1);   //垂直翻转V

        if (H && V)
            matrix.postScale(-1, -1);   //水平&垂直翻转HV

        return Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, true);

        //matrix.postRotate(-90);  //旋转-90度
    }

    public static String getPictureNameByDate(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return simpleDateFormat.format(date);
    }

    public static String getTmpPath(Context context){
        return context.getFilesDir() + File.separator + "tmp";
    }
    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String md5(String input) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(input.getBytes());
            return printHexBinary(bytes);
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }
    public static String getAPKVer() {
        PackageManager pm = PhotoApp.getAppContext().getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(PhotoApp.getAppContext().getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap adjustPhotoRotation(Bitmap source, int orientationDegree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(orientationDegree);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap getThumbnailPhoto(Bitmap src){
        int w,h,dw,dh;
        float sw;
        w = src.getWidth();
        h = src.getHeight();
        dw = 60;
        sw = dw*1.0f/w;
        Bitmap dst = Bitmap.createBitmap(60,(int)(h*sw), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dst);
        Matrix matrix = new Matrix();
        matrix.setScale(sw,sw);
        canvas.drawBitmap(src,matrix,new Paint());
        return dst;
    }


}
