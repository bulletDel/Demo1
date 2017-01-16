package com.demo.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.demo.R;
import com.demo.app.BaseApplication;
import com.demo.app.DemoApplication;
import com.demo.bean.BackMsg;
import com.demo.listener.BaseActivityListener;
import com.demo.listener.BaseFragmentListener;
import com.demo.listener.FragActViewListener;
import com.demo.listener.FragmentActivityListener;
import com.demo.listener.XHttpCallBack;
import com.demo.util.XPreferencesService;
import com.demo.views.XProgressDialog;


/**
 * fragment的基类
 * <p/>
 * Created by dmz on 2016/2/23.
 */
public abstract class BaseFragment extends Fragment implements
        BaseActivityListener, XHttpCallBack, FragActViewListener {

    private boolean isForeground;// 是否在前台执行
    protected XProgressDialog proDialog; // 对话框对象
    protected Dialog dialog; // 对话框对象
    protected View view;
    //    protected HttpUtils httpUtils;
    protected BaseApplication app;
    private XPreferencesService service;
    private BaseFragmentActivity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        app = DemoApplication.getInstance();
//        httpUtils = XUtilsHelp.getHttpUtils();
    }

    /**
     * 获取父监听器
     *
     * @return
     */
    public FragmentActivityListener getFragmentActivity() {
        return activity;
    }

    @Override
    public void onAttach(Activity activity2) {
        super.onAttach(activity2);
        if (!(activity2 instanceof BaseFragmentListener)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.listener = (BaseFragmentListener) activity2;
        }
        listener.addSubFragment(this);
        try {
            this.activity = (BaseFragmentActivity) activity2;
            this.activity.setFragActViewListener(this);
        } catch (ClassCastException e) {
            throw new RuntimeException(activity2.toString()
                    + " must implement BaseFragmentActivity");
        }
    }

    protected XPreferencesService getPreService() {
        if (service == null)
            service = DemoApplication.getInstance().getPreService();
        return service;
    }

    protected View findViewById(int id) {
        return view.findViewById(id);
    }

    /**
     * 必须使用当前View做为Fragment容器
     */
    public View getFragmentView() {
        return view;
    }

    //当前界面是否在前台运行
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
        return getActivity();
    }

    @Override
    public void onResume() {
        isForeground = true;
        super.onResume();
//        MobclickAgent.onPageStart(XSystemUtil.getAppliCationPackgeName(getContext()));
    }

    @Override
    public void onPause() {
        dialogCancel();
        isForeground = false;
        super.onPause();
//        MobclickAgent.onPageEnd(XSystemUtil.getAppliCationPackgeName(getContext()));
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(String text, int duration) {
        Toast.makeText(getContext(), text, duration).show();
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(int textId, int duration) {
        Toast.makeText(getContext(), textId, duration).show();
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出吐司对话信息
     */
    @Override
    public void showToast(int textId) {
        Toast.makeText(getContext(), textId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出多选选择 对话框
     */
    @Override
    public void showClickDialog(int titleId, String message, int cancel,
                                DialogInterface.OnClickListener cancelListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(getActivity()).setTitle(titleId)
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
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(getActivity()).setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(cancelId, cancelListener).create();
        dialog.show();
    }

    /**
     * 弹出多选选择 对话框
     */
    @Override
    public void showSelectDialog(int titleId, ListAdapter adapter,
                                 boolean outCannel, DialogInterface.OnClickListener selectedListener, int cancelId,
                                 DialogInterface.OnClickListener cancelListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(getActivity()).setTitle(titleId)
                .setSingleChoiceItems(adapter, -1, selectedListener)
                .setNegativeButton(cancelId, cancelListener).create();
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
        synchronized (BaseFragment.class) {
            if (!isForeground) {
                return;
            }
            dialogDismiss();
            if (proDialog == null)
                proDialog = new XProgressDialog(getActivity(), R.style.loadingDialog);
            proDialog.showDialog(info);
        }
    }


    /**
     * 弹出确认选择对话框
     */
    public void showSureDialog(boolean outCannel, int titleId, int messageId,
                               int positiveId, DialogInterface.OnClickListener positiveListener, int negativeId,
                               DialogInterface.OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(getActivity()).setTitle(titleId)
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
        if (!isForeground) {
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
        if (!isForeground) {
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
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(getActivity()).setTitle(titleId)
                .setMessage(message)
                .setPositiveButton(positiveId, positiveListener)
                .setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 弹出输入选择对话框
     */
    public void showInputDialog(boolean outCannel, int titleId, EditText view,
                                int positiveId, DialogInterface.OnClickListener positiveListener, int negativeId,
                                DialogInterface.OnClickListener negativeListener) {
        if (!isForeground) {
            return;
        }
        dialogDismiss();
        dialog = new AlertDialog.Builder(getActivity()).setTitle(titleId)
                .setView(view).setPositiveButton(positiveId, positiveListener)
                .setNegativeButton(negativeId, negativeListener).create();
        dialog.setCanceledOnTouchOutside(outCannel);
        dialog.show();
    }

    /**
     * 关闭对话框
     */
    public void dialogCancel() {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
        if (proDialog != null && proDialog.isShowing())
            proDialog.cancel();
        dialog = null;
        proDialog = null;
    }

    /**
     * 关闭对话框
     */
    @Override
    public void dialogDismiss() {
        if (proDialog != null && proDialog.isShowing())
            proDialog.cancel();
        proDialog = null;
        if (dialog != null && dialog.isShowing())
            dialog.hide();
    }

    @Override
    public void onPostcAsync(int status, BackMsg msg, boolean isSuccess) {
    }


    @Override
    public void qStartActivity(Context packageContext,
                               Class<? extends Activity> cls) {
        startActivity(new Intent(packageContext, cls));
    }

    @Override
    public void qStartActivity(String action) {
        startActivity(new Intent(action));
    }

    @Override
    public void qStartActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void qStartActivityForResult(String action, Uri uri, int flag) {
        startActivityForResult(new Intent(action), flag);
    }

    @Override
    public void qStartActivityForResult(Intent intent, int flag) {
        startActivityForResult(intent, flag);
    }

    @Override
    public void activityFinish() {
        dialogCancel();
        if (getActivity() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) getActivity()).activityFinish();
        }
    }

    @Override
    public void backDump() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        } else if (proDialog != null && proDialog.isShowing()) {
            proDialog.cancel();
        } else if (getActivity() instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) getActivity()).onLastFragment();
        }
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

    //当父FragmentActivity点击了返回按钮.(如果返回值为true,则由Fragment拦截该返回按钮事件)
    public boolean onBackpressed() {
        return false;
    }

    BaseFragmentListener listener;


    @Override
    public void onDetach() {
        listener.removeSubFragment(this);
        super.onDetach();
    }


    @Override
    public void onFragActViewClick(int resViewId, View resView) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
