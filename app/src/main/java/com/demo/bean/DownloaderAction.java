package com.demo.bean;

import android.app.Activity;

/**
 * 执行DownloaderService下载器，需传递参数 保存的实体类
 *
 * @author xmq
 */
public class DownloaderAction implements java.io.Serializable {
    /**
     * 下载url地址
     */
    public String url;
    /**
     * 本地保存地址
     */
    public String savePath;
    /**
     * 文件名称
     */
    public String saveName;
    /**
     * 点击通知将跳转Activity
     */
    public Class<? extends Activity> cls;
    /**
     * 是否需要通知栏更新下载进度,默认为会有通知
     */
    public boolean isNeedNotify = true;

    public DownloaderAction(String url, String savePath, String saveName,
                            Class<? extends Activity> cls) {
        this.url = url;
        this.savePath = savePath;
        this.saveName = saveName;
        this.cls = cls;
        isNeedNotify = true;
    }

    public DownloaderAction(String url, String savePath, String saveName, boolean isNeedNotify,
                            Class<? extends Activity> cls) {
        this.url = url;
        this.savePath = savePath;
        this.saveName = saveName;
        this.isNeedNotify = isNeedNotify;
        this.cls = cls;
    }

}