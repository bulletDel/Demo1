package com.demo.app;


import org.xutils.x;


/**
 * application自定义
 * <p>
 * Created by dmz on 2016/2/23.
 */
public class DemoApplication extends BaseApplication {

    private static DemoApplication mApplication;

    @Override
    public void onCreate() {
        mApplication = this;
        super.onCreate();
        initHttp();
    }


    /**
     * 初始化联网的http
     */
    private void initHttp() {
        x.Ext.init(this);
        x.Ext.setDebug(isDebug);
    }

    public synchronized static DemoApplication getInstance() {
        return mApplication;
    }
}
