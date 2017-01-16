package com.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.demo.app.BaseApplication;
import com.demo.app.DemoApplication;
import com.demo.base.BaseFragmentActivity;
import com.demo.network.XNetAsync;
import com.demo.util.XPreferencesService;

import org.xutils.http.RequestParams;

/**
 * 获取工具Activity父类
 *
 * @author xmq
 */
public abstract class CompontUtilFragmentActivity extends BaseFragmentActivity {
    private XPreferencesService service; // 保存参数管理类;
    protected BaseApplication app;
    //    protected HttpUtils http;
    //  protected BitmapUtils bitmapUtils;
    //  private Typeface tf;
    protected XNetAsync netAsync;
    protected RequestParams params;

    public XPreferencesService getPreService() {
        if (service == null)
            service = app.getPreService();
        return service;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        initActivity();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initActivity();
        //    XViewUtils.setTypeface(getLayoutInflater().inflate(layoutResID,null), tf);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initActivity();
        //    XViewUtils.setTypeface(view, tf);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initActivity();
        //    XViewUtils.setTypeface(view, tf);
    }

    /**
     * 初始化Activity
     */
    protected void initActivity() {
        app = DemoApplication.getInstance();
        BaseApplication.activitys.add(this);
//        http = XUtilsHelp.getHttpUtils();
        //    bitmapUtils = XUtilsHelp.getBitmapUtils(app);
        service = app.getPreService();// 参数保存类
        //    tf = app.getTypeFace();
        params = new RequestParams();
        //    XL.i(getClass().getSimpleName() + "当前内存为"
        //            + XSystemUtil.getAvailMemory(this) + "/"
        //            + XSystemUtil.getTotalMemory(this));
    }


    @Override
    protected void onStop() {
        dialogCancel();
        super.onStop();
    }

    @Deprecated
    public boolean isSetBackDraw(String backDraw, View view) {
        try {
            int point = backDraw.indexOf(".");
            if (point > 0) {
                int resId = getResources().getIdentifier(
                        backDraw.substring(0, point), "drawable",
                        getPackageName());
                if (resId > 0) {
                    view.setBackgroundResource(resId);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

}
