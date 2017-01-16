package com.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import com.demo.R;
import com.demo.app.BaseApplication;
import com.demo.app.DemoApplication;
import com.demo.bean.BackMsg;
import com.demo.listener.BaseActivity;
import com.demo.listener.BaseActivityListener;
import com.demo.listener.DialogClickListener;
import com.demo.listener.XHttpCallBack;
import com.demo.network.XNetAsync;
import com.demo.util.XConnectUtil;
import com.demo.util.XPreferencesService;

import org.xutils.http.RequestParams;


/**
 * 工具类的activity
 * <p/>
 * Created by dmz on 2016/2/23.
 */
public abstract class CompontUtilActivity extends BaseActivity implements View.OnClickListener, XHttpCallBack {

    /**
     * 保存参数的管理类
     */
    private XPreferencesService service; // 保存参数管理类;
    protected BaseApplication app;
    protected XNetAsync netAsync;
    protected RequestParams params;

    public XPreferencesService getPreService() {
        if (app == null) {
            app = DemoApplication.getInstance();
        }
        if (service == null)
            service = app.getPreService();
        return service;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivity();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initViewParams(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initViewParams(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initViewParams(view);
    }

    /**
     * 初始化参数, 该方法执行在setContentView以后
     * 使用字体（暂时不用）
     *
     * @param view
     */
    protected void initViewParams(View view) {
        //        XViewUtils.setTypeface(view, tf);
    }


    /**
     * 初始化Activity,该方法执行在 super.onCreate() 以后, setContentView()之前
     */
    protected void initActivity() {
        app = DemoApplication.getInstance();
        BaseApplication.activitys.add(this);
//        http = XUtilsHelp.getHttpUtils();
        service = app.getPreService();// 参数保存类
        //        tf = app.getTypeFace();
        params = new RequestParams();
    }

    @Override
    protected void onDestroy() {
        activityFinish();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        dialogCancel();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        } else {
            super.onBackPressed();
            activityFinish();
        }
    }

    public void backDump() {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.title_id_left) {
            activityFinish();
        }
    }


    /**
     * 移除当前Activity
     */
    public void activityFinish() {
        if (app != null || app.activitys != null) {
            app.activitys.remove(this);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            dialogCancel();
            finish();
        }
    }

    /**
     * 关闭应用Activity。
     *
     * @param exceptForAct 除了某个activity以外.
     *                     <p> 如果传入参数为null,则将所有activity全部都调用activityFinish()来销毁掉</p>
     */
    @Override
    public void appCancel(BaseActivityListener exceptForAct) {
        DemoApplication.getInstance().appCancel(exceptForAct);
    }

    /**
     * 判断是否有网络连接
     */
    protected boolean checkNet() {
        if (!XConnectUtil.isNetworkConnected(this)) {
            showSureDialog(false, R.string.dialog_title, R.string.dialog_message_noNet, R.string.dialog_setting, new DialogClickListener(1, this), R.string.dialog_cannel, new DialogClickListener(2, this));
            return false;
        }
        return true;
    }


    @Override
    public void onPostcAsync(int status, BackMsg msg, boolean isSuccess) {

    }

}
