package com.demo.app;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.demo.R;
import com.demo.listener.AppInterface;
import com.demo.listener.BaseActivityListener;
import com.demo.util.XPreferencesService;

import java.io.File;
import java.util.LinkedList;


/**
 * 框架集成的父Application.
 */
public abstract class BaseApplication extends Application implements
        AppInterface {
    public static boolean isDebug = true; //当前是否为调试模式

    public static LinkedList<BaseActivityListener> activitys; //保存所有带有界面的控件集合
    private XPreferencesService service; //sharedPerferences 封装工具类， 待优化
    /**
     * 版本信息，项目总保存路径，皮肤文件路径，缓存文件路径，数据文件路径
     */
    private String version, dir, skinBasePath, cachePath, dataPath;


    @Override
    public void onCreate() {
        super.onCreate();
        activitys = new LinkedList<BaseActivityListener>(); // activity容器
        service = new XPreferencesService();// 设置参数保存类，实例化
        dataPath = getSaveDir() + getString(R.string.data);
        skinBasePath = getSaveDir() + getString(R.string.skin);
        isDebug = Boolean.parseBoolean(getString(getResources().getIdentifier(
                "is_debug", "string", getPackageName())));
        getSkinSaveDir();
        getCacheDir();
        getDataDir1();
        initImageLoader();
    }

    public void initImageLoader() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) this.getBaseContext().getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(mDisplayMetrics);
    }

    /**
     * 关闭应用Activity。
     *
     * @param exceptForAct 除了某个activity以外.
     *                     <p> 如果传入参数为null,则将所有activity全部都调用activityFinish()来销毁掉</p>
     */
    public void appCancel(BaseActivityListener exceptForAct) {
        //是否有传入不finish的activity,为null则是全部finish掉
        boolean isContainSelf = exceptForAct == null;
        if (activitys != null) {
            LinkedList<BaseActivityListener> list = (LinkedList<BaseActivityListener>) activitys.clone();
            for (int i = 0, len = list.size(); i < len; i++) {
                BaseActivityListener activity = list.get(i);
                if (activity != null) {
                    if (!isContainSelf
                            && activity.getClass().getName()
                            .equalsIgnoreCase(exceptForAct.getClass().getName())) {
                        //将状态职位true,则后续将会将其他的全部finish掉
                        isContainSelf = true;
                        continue;
                    }
                    activity.activityFinish();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.xmq.mode.listener.AppInterface#getSaveDir()
     */
    @Override
    public String getSaveDir() {
        if (dir == null) {// 当第一次 获取路径时，初始化路劲dir
            String parentPath = getResources().getString(R.string.parentPath);
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                dir = Environment.getExternalStorageDirectory().toString() + parentPath;// 获取SD卡根目录
            } else {
                dir = getFilesDir().getAbsolutePath() + parentPath;
            }
            File file = new File(dir);
            if (!file.exists())
                file.mkdirs();
        }
        return dir;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.xmq.mode.listener.AppInterface#getSkinSaveDir()
     */
    @Override
    public String getSkinSaveDir() {
        if (skinBasePath == null)
            skinBasePath = getSaveDir() + getString(R.string.skin);
        File skinDir = new File(skinBasePath);
        if (!skinDir.exists() || !skinDir.isDirectory())
            skinDir.mkdirs();
        return skinBasePath;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.xmq.mode.listener.AppInterface#getAppCacheDir()
     */
    @Override
    public String getAppCacheDir() {
        if (cachePath == null) {
            cachePath = getSaveDir() + getString(R.string.cache);
        }
        File cacheDir = new File(cachePath);
        if (!cacheDir.exists() || !cacheDir.isDirectory())
            cacheDir.mkdirs();
        if (cachePath == null) {
            cachePath = "";
        }
        return cachePath;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.xmq.mode.listener.AppInterface#getDataDir1()
     */
    @Override
    public String getDataDir1() {
        if (dataPath == null) {
            dataPath = getSaveDir() + getString(R.string.data);
        }
        File dataDir = new File(dataPath);
        if (!dataDir.exists() || !dataDir.isDirectory())
            dataDir.mkdirs();
        return dataPath;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.xmq.mode.listener.AppInterface#getPreService()
     */
    @Override
    public XPreferencesService getPreService() {
        if (service == null)
            service = new XPreferencesService();
        return service;
    }

    /*
     * (non-Javadoc)
     * @see com.xmq.mode.listener.AppInterface#getVersion()
     */
    @Override
    public String getVersion() {
        if (version == null) {
            try {
                version = getPackageManager().getPackageInfo(getPackageName(),
                        0).versionName;
            } catch (NameNotFoundException e) {
                return "";
            }
        }
        return version;
    }




//    /**
//     * 检查是否存在皮肤包文件，有则判断是否是第一次使用该皮肤包，不是则解压到相应文件夹下
//     */
//    @Deprecated
//    public void checkSkin() {
//        File file = new File(getSaveDir(), "skin.zip");
//        File file2 = new File(skinBasePath);
//        String skinName = "0ks";
//        if (file.exists() || (file2 != null && !file2.exists())) {
//            skinName = file.length() + "ks";
//            // 把zip格式的资源包解压到指定路径下,并保存参数为他的包的大小
//            if (!skinName.equals(service.getInfo(this, "skinName", ""))) {
//                service.saveInfo(this, "skinName", skinName);
//                XZipUtil.unzip(file, skinBasePath);
//            }
//        }
//        service.saveInfo(this, "skinName", skinName);
//    }

}
