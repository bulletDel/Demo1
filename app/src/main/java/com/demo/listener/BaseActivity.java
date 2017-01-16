package com.demo.listener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.R;
import com.demo.views.XProgressDialog;


/**
 * activity的基类
 * <p/>
 * Created by dmz on 2016/2/23.
 */
public abstract class BaseActivity extends Activity implements BaseActivityListener {

    /**
     * 消息提示窗口
     */
    protected XProgressDialog proDialog;
    /**
     * activity是否在前台运行
     */
    private boolean isForeground;// 是否在前台执行

    /**
     * 对话框对象
     */
    protected Dialog dialog; // 对话框对象


    @Override
    public boolean isForeground() {
        return isForeground;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        MobclickAgent.openActivityDurationTrack(false);//禁止默认的友盟调用方案
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        isForeground = true;
    }

    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        dialogDismiss();
        isForeground = false;
        super.onPause();
    }

    @Override
    public String getIdString(int resId) {
        return getString(resId);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 界面跳转
     */
    @Override
    public void qStartActivity(Context packageContext,
                               Class<? extends Activity> cls) {
        qStartActivity(new Intent(packageContext, cls));

    }

    /**
     * 界面跳转
     */
    @Override
    public void qStartActivity(String action) {
        qStartActivity(new Intent(action));
    }

    /**
     * 界面跳转
     */
    @Override
    public void qStartActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }

    /**
     * 界面跳转,获取回传值
     */
    @Override
    public void qStartActivityForResult(String action, Uri uri, int flag) {
        qStartActivityForResult(new Intent(action, uri), flag);
    }

    /**
     * 界面跳转,获取回传值
     */
    @Override
    public void qStartActivityForResult(Intent intent, int flag) {
        startActivityForResult(intent, flag);
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(String text, int duration) {
        Toast.makeText(this, text, duration).show();
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(int textId, int duration) {
        Toast.makeText(this, textId, duration).show();
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(int textId) {
        Toast.makeText(this, textId, Toast.LENGTH_SHORT).show();
    }


    /**
     * 弹出多选选择 对话框
     */
    @Override
    public void showClickDialog(int titleId, String message, int cancel,
                                DialogInterface.OnClickListener cancelListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId)
                .setMessage(message).setPositiveButton(cancel, cancelListener)
                .create();
        dialog.show();
    }

    /**
     * 弹出多选选择 对话框
     */
    @Override
    public void showClickDialog(int titleId, int messageId, int cancelId,
                                DialogInterface.OnClickListener cancelListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(cancelId, cancelListener).create();
        dialog.show();
    }

    @Override
    public void showProgressDialog(String msg) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        if (proDialog == null)
            proDialog = new XProgressDialog(this, R.style.loadingDialog);
        dialogDismiss();
        proDialog.showDialog(msg);
    }

    @Override
    public void showProgressDialog(int infoId) {
        showProgressDialog(getString(infoId));
    }

    /**
     * 弹出确认选择对话框
     */
    public void showSureDialog(boolean outCannel, int titleId, int messageId,
                               int positiveId, DialogInterface.OnClickListener positiveListener, int negativeId,
                               DialogInterface.OnClickListener negativeListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(positiveId, positiveListener)
                .setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    @Override
    public void showSureDialog(Context ctx, boolean outCannel, int titleId,
                               String message, int positiveId, DialogInterface.OnClickListener positiveListener,
                               int negativeId, DialogInterface.OnClickListener negativeListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(ctx).setTitle(titleId)
                .setMessage(message)
                .setPositiveButton(positiveId, positiveListener)
                .setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出确认选择对话框
     */
    public void showSureDialog(Context ctx, boolean outCannel, int titleId,
                               int messageId, int positiveId, DialogInterface.OnClickListener positiveListener,
                               int negativeId, DialogInterface.OnClickListener negativeListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(ctx).setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(positiveId, positiveListener)
                .setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出确认选择对话框
     */
    public void showSureDialog(boolean outCannel, int titleId, String message,
                               int positiveId, DialogInterface.OnClickListener positiveListener, int negativeId,
                               DialogInterface.OnClickListener negativeListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        if (dialog == null) {
            dialog = new AlertDialog.Builder(this).setTitle(titleId)
                    .setMessage(message)
                    .setPositiveButton(positiveId, positiveListener)
                    .setNegativeButton(negativeId, negativeListener).create();
            dialog.setCanceledOnTouchOutside(outCannel);
        }
        dialog.show();
    }

    /**
     * 弹出输入选择对话框
     */
    public void showInputDialog(boolean outCannel, int titleId, EditText view,
                                int positiveId, DialogInterface.OnClickListener positiveListener, int negativeId,
                                DialogInterface.OnClickListener negativeListener) {
        if (!isForeground()) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setView(view)
                .setPositiveButton(positiveId, positiveListener)
                .setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 关闭对话框
     */
    public void dialogCancel() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        if (proDialog != null && proDialog.isShowing())
            proDialog.dismiss();
        dialog = null;
        proDialog = null;
    }

    /**
     * 关闭对话框
     */
    @Override
    public void dialogDismiss() {
        if (proDialog != null && proDialog.isShowing())
            proDialog.hide();
        if (dialog != null && dialog.isShowing())
            dialog.hide();
    }

}
