package com.demo.listener;

import android.content.DialogInterface;

/**
 * 对话框点击事件
 */
public class DialogClickListener implements DialogInterface.OnClickListener {
    private int index;
    private BaseActivityListener activity;

    public DialogClickListener(int index, BaseActivityListener activity) {
        this.index = index;
        this.activity = activity;
    }


    public void onClick(DialogInterface dialog, int which) {
        switch (index) {
            case 1:// 打开蓝牙查找设备界面
                activity.qStartActivity(android.provider.Settings.ACTION_WIFI_SETTINGS);// 跳转到无线wifi网络设置界面
            case 2:
                activity.activityFinish();
                break;
        }
    }
}
