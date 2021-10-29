package com.gxx.jiaguapplication;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.DIRECTORY_PICTURES;

public class FileSDCardUtil {
    public static FileSDCardUtil fileSDCardUtil;
    public final static String ConfigDirs = "configDirs";//配置的目录，不可随意创建不需要的文件目录

    public static FileSDCardUtil getInstance() {
        if (fileSDCardUtil == null) {
            synchronized (FileSDCardUtil.class) {
                if (fileSDCardUtil == null) {
                    fileSDCardUtil = new FileSDCardUtil();
                }
            }
        }
        return fileSDCardUtil;
    }

    /**
     * @date 创建时间:2020/5/22 0022
     * @auther gaoxiaoxiong
     * @Descriptiion 获取沙盒目录下，config目录里面的路径，里面会包含很多的文件夹
     **/
    public String getSandboxConfigDirsPublickDisk(Context context, String dir) {
        return getSandboxPublickDiskFileDir(context.getApplicationContext(), ConfigDirs + File.separator + dir);
    }


    /**
     * @date: 2019/5/22 0022
     * @author: gaoxiaoxiong
     * @description:获取沙盒目录下图片位置
     **/
    public String getSandboxPublickDiskImagePicDir(Context context) {
        return getSandboxPublickDiskFileDir(context.getApplicationContext(), DIRECTORY_PICTURES);
    }


    /**
     * @date: 2019/5/22 0022
     * @author: gaoxiaoxiong
     * @description: 获取沙盒目录下电影位置
     **/
    public String getSandboxPublickDiskMoviesDir(Context context) {
        return getSandboxPublickDiskFileDir(context.getApplicationContext(), DIRECTORY_MOVIES);
    }

    /**
     * @date :2019/12/16 0016
     * @author : gaoxiaoxiong
     * @description:获取沙盒目录下音乐位置
     **/
    public String getSandboxPublickDiskMusicDir(Context context) {
        return getSandboxPublickDiskFileDir(context.getApplicationContext(), DIRECTORY_MUSIC);
    }


    /**
     * 作者：GaoXiaoXiong
     * 创建时间:2019/7/21
     * 注释描述:沙盒目录下文件保存位置
     */
    public String getSandboxPublickDiskDownLoadsDir(Context context) {
        return getSandboxPublickDiskFileDir(context.getApplicationContext(), DIRECTORY_DOWNLOADS);
    }

    /**
     * @date 创建时间:2020/6/5 0005
     * @auther gaoxiaoxiong
     * @Descriptiion 缓存--- 获取沙盒的缓存的目录，config目录里面的路径，里面会包含很多的文件夹
     **/
    public String getSandboxConfigDirsCacheDisk(Context context, String dir) {
        return getSandboxPublickDiskCacheDir(context.getApplicationContext(), ConfigDirs + File.separator + dir);
    }



    /**
     * @date 创建时间:2018/12/20
     * @author GaoXiaoXiong
     * @Description: 缓存--- 获取沙盒目录下的图片
     */
    public String getSandboxPublickDiskImagePicCacheDir(Context context) {
        return getSandboxPublickDiskCacheDir(context.getApplicationContext(), DIRECTORY_PICTURES);
    }


    /**
     * @date: 2019/6/21 0021
     * @author: gaoxiaoxiong
     * @description:缓存--- 获取沙盒目录下的音乐 缓存目录
     **/
    public String getSandboxPublickDiskMusicCacheDir(Context context) {
        return getSandboxPublickDiskCacheDir(context.getApplicationContext(), DIRECTORY_MUSIC);
    }

    /**
     * @date: 创建时间:2019/12/22
     * @author: gaoxiaoxiong
     * @descripion:缓存--- 获取沙盒目录下的电影 缓存目录
     **/
    public String getSandboxPublickDiskMoviesCacheDir(Context context) {
        return getSandboxPublickDiskCacheDir(context.getApplicationContext(), DIRECTORY_MOVIES);
    }





    /**
     * 作者：GaoXiaoXiong
     * 创建时间:2019/1/26
     * 注释描述:获取缓存目录
     * @fileName 获取沙盒存储目录下缓存的 fileName的文件夹路径
     */
    public String getSandboxPublickDiskCacheDir(Context context, String fileName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {//此目录下的是外部存储下的私有的fileName目录
            cachePath = context.getExternalCacheDir().getPath() + File.separator + fileName;  //SDCard/Android/data/你的应用包名/cache/fileName
        } else {
            cachePath = context.getCacheDir().getPath() + File.separator + fileName;
        }
        File file = new File(cachePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath(); //SDCard/Android/data/你的应用包名/cache/fileName
    }

    /**
     * @date: 2019/8/2 0002
     * @author: gaoxiaoxiong
     * @description:获取沙盒存储目录下的 fileName的文件夹路径
     **/
    public String getSandboxPublickDiskFileDir(Context context, String fileName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {//此目录下的是外部存储下的私有的fileName目录
            cachePath = context.getExternalFilesDir(fileName).getAbsolutePath();  //mnt/sdcard/Android/data/com.my.app/files/fileName
        } else {
            cachePath = context.getFilesDir().getPath() + File.separator + fileName;        //data/data/com.my.app/files/fileName
        }
        File file = new File(cachePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();  //mnt/sdcard/Android/data/com.my.app/files/fileName
    }


    /**
     * @date :2020/3/17 0017
     * @author : gaoxiaoxiong
     * @description:获取公共目录，注意，只适合android9.0以下的
     **/
    public String getPublickDiskFileDirAndroid9(String fileDir) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = Environment.getExternalStoragePublicDirectory(fileDir).getPath();
        }
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * @date 创建时间:2020/11/6 0006
     * @auther gaoxiaoxiong
     * @Descriptiion mnt/sdcard/ fileDir
     **/
    public String getPublickExternalStorageDirectory9(String fileDir) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageDirectory())
                || !Environment.isExternalStorageRemovable()) {
            filePath = Environment.getExternalStorageDirectory().getPath();
        }
        File file = new File(filePath + fileDir);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }



    /**
     * @date 创建时间:2018/12/20
     * @author GaoXiaoXiong
     * @Description: 缓存--- 获取沙盒目录下的图片
     */
    public String getInternalPublickDiskImagePicCacheDir(Context context) {
        return getInternalDiskCacheFileDir(context.getApplicationContext(), DIRECTORY_PICTURES);
    }

    /**
     * @date: 2019/6/21 0021
     * @author: gaoxiaoxiong
     * @description:缓存--- 获取沙盒目录下的音乐 缓存目录
     **/
    public String getInternalPublickDiskMusicCacheDir(Context context) {
        return getInternalDiskCacheFileDir(context.getApplicationContext(), DIRECTORY_MUSIC);
    }

    /**
     * @date: 创建时间:2019/12/22
     * @author: gaoxiaoxiong
     * @descripion:缓存--- 获取沙盒目录下的电影 缓存目录
     **/
    public String getInternalPublickDiskMoviesCacheDir(Context context) {
        return getInternalDiskCacheFileDir(context.getApplicationContext(), DIRECTORY_MOVIES);
    }





    /**
     * @date: 2019/5/22 0022
     * @author: gaoxiaoxiong
     * @description:获取内部存储目录下图片位置
     **/
    public String getInternalDiskImagePicDir(Context context) {
        return getInternalDiskFileDir(context.getApplicationContext(), DIRECTORY_PICTURES);
    }


    /**
     * @date: 2019/5/22 0022
     * @author: gaoxiaoxiong
     * @description: 获取内部存储目录下电影位置
     **/
    public String getInternalDiskMoviesDir(Context context) {
        return getInternalDiskFileDir(context.getApplicationContext(), DIRECTORY_MOVIES);
    }

    /**
     * @date :2019/12/16 0016
     * @author : gaoxiaoxiong
     * @description:获取内部存储目录下音乐位置
     **/
    public String getInternalDiskMusicDir(Context context) {
        return getInternalDiskFileDir(context.getApplicationContext(), DIRECTORY_MUSIC);
    }


    /**
     * @date 创建时间:2021/10/8 0008
     * @auther gaoxiaoxiong
     * @Descriptiion 内部存储目录
     **/
    public String getInternalDiskFileDir(Context context, String fileDir) {
        String filePath = context.getFilesDir().getAbsolutePath() + File.separator + fileDir;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    /**
     * @date 创建时间:2021/10/8 0008
     * @auther gaoxiaoxiong
     * @Descriptiion 内部缓存存储目录
     **/
    public String getInternalDiskCacheFileDir(Context context, String fileDir) {
        String filePath = context.getCacheDir().getAbsolutePath() + File.separator + fileDir;
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

}
