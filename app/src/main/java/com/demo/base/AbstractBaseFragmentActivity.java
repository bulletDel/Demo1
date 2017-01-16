package com.demo.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.SparseArray;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.demo.R;
import com.demo.app.BaseApplication;
import com.demo.app.DemoApplication;
import com.demo.bean.BackMsg;
import com.demo.listener.BaseActivityListener;
import com.demo.listener.BaseFragmentListener;
import com.demo.listener.FragmentActivityListener;
import com.demo.listener.XHttpCallBack;
import com.demo.views.XProgressDialog;


abstract class AbstractBaseFragmentActivity extends FragmentActivity implements
        BaseActivityListener, XHttpCallBack, FragmentActivityListener, BaseFragmentListener, OnBackStackChangedListener {
    private boolean isForeground;// 是否在前台执行
    protected XProgressDialog proDialog; // 对话框对象
    protected Dialog dialog; // 对话框对象
    /**
     * 默认子碎片Fragment的数量
     */
    protected final SparseArray<BaseFragment> subList = new SparseArray<BaseFragment>();


    @Override
    public boolean isForeground() {
        return isForeground;
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

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        dialogDismiss();
        isForeground = false;
        super.onPause();
//        MobclickAgent.onPause(this);
    }


    @Override
    public void onBackPressed() {
        for (int i = 0, size = subList.size(); i < size; i++) {
            BaseFragment fragment = subList.valueAt(i);
            if (fragment == null || !fragment.onBackpressed()) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.cancel();
                    dialog = null;
                } else if (proDialog != null && proDialog.isShowing()) {
                    proDialog.cancel();
                    dialog = null;
                } else
                    onLastFragment();
            }
        }

    }

    @Override
    public void onBackStackChanged() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0)
            activityFinish();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        BaseApplication.activitys.add(this);
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
    public void showClickDialog(int titleId, String message, int cancel, OnClickListener cancelListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setMessage(message).setPositiveButton(cancel, cancelListener).create();
        dialog.show();
    }

    /**
     * 弹出多选选择 对话框
     */
    @Override
    public void showClickDialog(int titleId, int messageId, int cancelId, OnClickListener cancelListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setMessage(messageId).setPositiveButton(cancelId, cancelListener).create();
        dialog.show();
    }

    /**
     * 弹出多选选择 对话框
     */
    @Override
    public void showSelectDialog(int titleId, ListAdapter adapter, boolean outCannel, OnClickListener selectedListener, int cancelId, OnClickListener cancelListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setSingleChoiceItems(adapter, -1, selectedListener).setNegativeButton(cancelId, cancelListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出圆形等待对话框，canneled是否响应返回键取消操作
     */
    @Override
    public void showProgressDialog(int infoId) {
        showProgressDialog(getString(infoId));
    }

    /**
     * 弹出圆形等待对话框，canneled是否响应返回键取消操作
     */
    @Override
    public void showProgressDialog(String info) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        if (proDialog == null)
            proDialog = new XProgressDialog(this, R.style.loadingDialog);
        dialogDismiss();
        proDialog.showDialog(info);
    }

    /**
     * 弹出确认选择对话框
     */

    public void showSureDialog(boolean outCannel, int titleId, int messageId, int positiveId, OnClickListener positiveListener, int negativeId, OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setMessage(messageId).setPositiveButton(positiveId, positiveListener).setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    @Override
    public void showSureDialog(Context ctx, boolean outCannel, int titleId, String message, int positiveId, OnClickListener positiveListener, int negativeId, OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(ctx).setTitle(titleId).setMessage(message).setPositiveButton(positiveId, positiveListener).setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出确认选择对话框
     */
    public void showSureDialog(Context ctx, boolean outCannel, int titleId, int messageId, int positiveId, OnClickListener positiveListener, int negativeId, OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(ctx).setTitle(titleId).setMessage(messageId).setPositiveButton(positiveId, positiveListener).setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出确认选择对话框
     */
    public void showSureDialog(boolean outCannel, int titleId, String message, int positiveId, OnClickListener positiveListener, int negativeId, OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setMessage(message).setPositiveButton(positiveId, positiveListener).setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出输入选择对话框
     */
    public void showInputDialog(boolean outCannel, int titleId, EditText view, int positiveId, OnClickListener positiveListener, int negativeId, OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(this).setTitle(titleId).setView(view).setPositiveButton(positiveId, positiveListener).setNegativeButton(negativeId, negativeListener).create();
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

    @Override
    public void qStartActivity(Context packageContext, Class<? extends Activity> cls) {
        qStartActivity(new Intent(packageContext, cls));
    }

    @Override
    public void qStartActivity(String action) {
        qStartActivity(new Intent(action));
    }

    @Override
    public void qStartActivity(Intent intent) {
        this.startActivity(intent);
    }

    @Override
    public void qStartActivityForResult(String action, Uri uri, int flag) {
        this.startActivityForResult(new Intent(action), flag);
    }

    @Override
    public void qStartActivityForResult(Intent intent, int flag) {
        this.startActivityForResult(intent, flag);
    }

    /**
     * 关闭应用Activity。
     *
     * @param exceptForAct 除了某个activity以外.
     *                     <p> 如果传入参数为null,则将所有activity全部都调用activityFinish()来销毁掉</p>
     */
    public void appCancel(BaseActivityListener exceptForAct) {
        DemoApplication.getInstance().appCancel(exceptForAct);
    }

    /**
     * 移除当前Activity
     */
    public void activityFinish() {
        if (BaseApplication.activitys != null || BaseApplication.activitys != null) {
            BaseApplication.activitys.remove(this);
        }
        dialogCancel();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    public void backDump() {
        onLastFragment();
    }

    @Override
    public void onPostcAsync(int status, BackMsg msg, boolean isSuccess) {
    }


}