package com.demo.listener;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.ListAdapter;

/**
 * 抽象Activity/FragmentActivity/Fragment的界面基本功能方法接口
 *
 * @author xmq
 */
public interface BaseListener {
    /**
     * 根据资源Id获取字符串
     */
    String getIdString(int resId);

    /**
     * 获取上下文对象
     */
    Context getContext();

    /**
     * 获取当前活动Activity
     */
    Activity getActivity();


    /**
     * 判断当前界面是否还在前台
     */
    boolean isForeground();

    /**
     * 弹出点击选择 对话框
     */
    void showClickDialog(int titleId, String message, int cancel,
                         DialogInterface.OnClickListener cancelListener);

    /**
     * 弹出多选选择 对话框
     */
    void showClickDialog(int titleId, int messageId, int cancelId,
                         DialogInterface.OnClickListener cancelListener);

    /**
     * 弹出多选选择 对话框
     */
    void showSelectDialog(int titleId, ListAdapter adapter,
                          boolean outCannel,
                          DialogInterface.OnClickListener selectedListener, int cancelId,
                          DialogInterface.OnClickListener cancelListener);

    /**
     * 弹出圆形等待对话框
     */
    void showProgressDialog(int infoId);

    /**
     * 弹出圆形等待对话框
     */
    void showProgressDialog(String infoId);


    /**
     * 弹出确认选择对话框
     */
    void showSureDialog(boolean outCannel, int titleId, int messageId,
                        int positiveId, DialogInterface.OnClickListener positiveListener,
                        int negativeId, DialogInterface.OnClickListener negativeListener);

    /**
     * 弹出确认选择对话框
     */
    void showSureDialog(boolean outCannel, int titleId, String message,
                        int positiveId, DialogInterface.OnClickListener positiveListener,
                        int negativeId, DialogInterface.OnClickListener negativeListener);

    /**
     * 弹出确认选择对话框
     */
    void showSureDialog(Context ctx, boolean outCannel, int titleId,
                        String message, int positiveId,
                        DialogInterface.OnClickListener positiveListener, int negativeId,
                        DialogInterface.OnClickListener negativeListener);

    /**
     * 弹出输入选择对话框
     */
    void showInputDialog(boolean outCannel, int titleId, EditText view,
                         int positiveId, DialogInterface.OnClickListener positiveListener,
                         int negativeId, DialogInterface.OnClickListener negativeListener);

    /**
     * 关闭对话框
     */
    void dialogDismiss();

    /**
     * 隐藏对话框
     */
    void dialogCancel();

    /**
     * 弹出吐司对话信息
     */
    void showToast(String text, int duration);

    /**
     * 弹出吐司对话信息,默认时间2秒
     */
    void showToast(String text);

    /**
     * 弹出吐司对话信息
     */
    void showToast(int textId, int duration);

    /**
     * 弹出吐司对话信息,默认时间2秒
     */
    void showToast(int textId);


}
