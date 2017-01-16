package com.demo.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 抽象Activity方法接口
 *
 * @author xmq
 */
public interface BaseActivityListener extends BaseListener {

    /**
     * 界面跳转
     */
    void qStartActivity(Context packageContext,
                        Class<? extends Activity> cls);

    /**
     * 界面跳转
     */
    void qStartActivity(String action);

    /**
     * 界面跳转
     */
    void qStartActivity(Intent intent);

    /**
     * 界面跳转,获取回传值
     */
    void qStartActivityForResult(String action, Uri uri, int flag);

    void qStartActivityForResult(Intent intent, int flag);

    void activityFinish();

    /**
     * 点击返回按钮回调方法
     */
    void backDump();

    /**
     * 关闭应用Activity。
     *
     * @param exceptForAct 除了某个activity以外.
     *                     <p> 如果传入参数为null,则将所有activity全部都调用activityFinish()来销毁掉</p>
     */
    void appCancel(BaseActivityListener exceptForAct);

}
