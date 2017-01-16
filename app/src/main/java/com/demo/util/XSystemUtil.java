package com.demo.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * 系统系列组件操作工具
 *
 * @author pc
 */
public class XSystemUtil {

    private XSystemUtil() {
    /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取android手机设备的唯一标识 ANDROID_ID
     */
    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本比对方法
     *
     * @param oldVersion 现在使用版本
     * @param newVersion 最新版本
     * @return true 需要更新
     */
    public static boolean checkVersion(String oldVersion, String newVersion) {
        int[] ver1 = getVersion(oldVersion);
        int[] ver2 = getVersion(newVersion);
        for (int i = 0; i < 3; i++) {
            if (ver1[i] != ver2[i])
                return ver1[i] < ver2[i];
        }
        return false;
    }

    /**
     * 获取版本名称 versionName, 如1.0.1;
     *
     * @param version
     */
    private static int[] getVersion(String version) {
        int[] ver = new int[3];
        for (int i = 0; i < 3; i++) {
            String sv = "0";
            int start = version.indexOf(".");
            if (start == -1) {
                sv = version.substring(0, version.length());
                ver[i] = Integer.parseInt(sv);
                break;
            } else {
                sv = version.substring(0, start);
                ver[i] = Integer.parseInt(sv);
                version = version.substring(start + 1);
            }
        }
        return ver;
    }

    /**
     * 获取可用内存大小
     *
     * @param ctx
     * @return 格式化信息
     */
    public static String getAvailMemory(Context ctx) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) ctx
                .getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(ctx, mi.availMem);// 将获取的内存大小规格化
    }

    /**
     * 获取系统全部内存大小
     *
     * @param ctx
     * @return 格式化信息
     */
    public static long getTotalMemory(Context ctx) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            String str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory;// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获取系统全部内存大小
     *
     * @param ctx
     * @return 格式化信息
     */
    public static String getTotalMemoryFormat(Context ctx) {
        return Formatter.formatFileSize(ctx, getTotalMemory(ctx));// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 安装下载后的apk文件
     */
    public static void Instanll(Context ctx, File file) {
        if (file != null && file.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            ctx.startActivity(intent);
        }
    }

//  /**
//   判断手机中是否有指定包名的应用程序
//   */
//  public static boolean checkBrowser(Context context, String packageName) {
//    if (packageName == null || "".equals(packageName))
//      return false;
//    try {
//      ApplicationInfo info = context.getPackageManager()
//          .getApplicationInfo(packageName,
//              PackageManager.GET_UNINSTALLED_PACKAGES);
//      return true;
//    } catch (PackageManager.NameNotFoundException e) {
//      return false;
//    }
//  }

    /**
     * 打开某个应用程序
     */
    public static void openApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);
        List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (ri != null) {
            String pn = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(pn, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * 获取应用程序的包名
     *
     * @param context
     * @return 包名
     */
    public static String getAppliCationPackgeName(Context context) {
        String packageName = "";
        if (null != context) {
            packageName = context.getPackageName();
        }
        return packageName;
    }

}
