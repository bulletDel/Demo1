package com.demo.network;

import android.content.DialogInterface;

import com.demo.R;
import com.demo.bean.BackMsg;
import com.demo.listener.BaseActivityListener;
import com.demo.listener.XHttpCallBack;
import com.demo.util.XL;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 网络任务异步访问封装
 *
 * @author xmq
 */
public abstract class XNetAsync {
    protected BaseActivityListener act;
    protected int status; // 标识，状态
    protected XHttpCallBack back;
    private String url, waitInfo, failInfo; //网络url，等待显示的信息，失败显示的信息
    private ArrayList<String> backHeads; //需要在请求后得到的头部数据信息。
    private RequestParams params; //请求的参数
    private DialogInterface.OnCancelListener cancelListener;
    boolean isRequesting,  //是否正在进行请求
            isNeedProgressDialog = true, //是否需要显示进度对话框，默认为显示
            isCancelable; //是否允许 中断请求，默认为false
    protected Callback.Cancelable callable;
    private int connectTime = 10,//连接超时时间(单位:秒) 默认10秒
            socketTime = 30;//传输超时时间(单位:秒) 默认30秒


    public XNetAsync(BaseActivityListener activity, XHttpCallBack back, int waitResId, int failResId, int status) {
        this(activity, back, waitResId, failResId, status, true);
    }

    public XNetAsync(BaseActivityListener activity, XHttpCallBack back, int waitResId, int failResId, int status, boolean isNeedProgressDialog) {
        this(activity, back, activity.getIdString(waitResId), activity.getIdString(failResId), status, isNeedProgressDialog);
    }

    public XNetAsync(BaseActivityListener activity, XHttpCallBack back, String waitInfo, String failInfo, int status, boolean isNeedProgressDialog) {
        this.act = activity;
        if (isNeedProgressDialog)
            this.waitInfo = waitInfo;
        this.failInfo = failInfo;
        this.back = back;
        this.status = status;
        this.isNeedProgressDialog = isNeedProgressDialog;
        this.cancelListener = new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                stopNetWork();
            }
        };
    }

    public XNetAsync(BaseActivityListener activity, XHttpCallBack back, int waitResId, int failResId, int status,
                     boolean isNeedProgressDialog, DialogInterface.OnCancelListener listener) {
        this(activity, back, activity.getIdString(waitResId), activity.getIdString(failResId), status, isNeedProgressDialog, listener);
    }

    public XNetAsync(BaseActivityListener activity, XHttpCallBack back, String waitInfo, String failInfo,
                     int status, boolean isNeedProgressDialog, DialogInterface.OnCancelListener listener) {
        this.act = activity;
        if (isNeedProgressDialog)
            this.waitInfo = waitInfo;
        this.failInfo = failInfo;
        this.back = back;
        this.status = status;
        this.isNeedProgressDialog = isNeedProgressDialog;
        this.cancelListener = listener;
    }

    /**
     * 发起Http访问方法
     *
     * @param url    访问地址
     * @param params 请求参数
     * @param status 标识码
     */
    public void send(String url, RequestParams params, int status) {
        this.url = url;
        this.params = params;
        this.status = status;
        start();
    }

    /**
     * 发起Http访问方法
     *
     * @param url    访问地址
     * @param params 请求参数
     */
    public void send(String url, RequestParams params) {
        send(url, params, status);
    }

    /**
     * 发起Http访问方法
     *
     * @param url       访问地址
     * @param params    请求参数
     * @param backHeads 需要返回的头部信息键值对
     */
    public void send(String url, RequestParams params, ArrayList<String> backHeads) {
        this.url = url;
        this.params = params;
        this.backHeads = backHeads;
        start();
    }

    /**
     * 设置访问连接超时时间
     *
     * @param connectTime
     */
    public void setConnectTime(int connectTime) {
        this.connectTime = connectTime;
    }

    /**
     * 设置传输 超时时间
     *
     * @param socketTime
     */
    public void setSocketTime(int socketTime) {
        this.socketTime = socketTime;
    }

    /**
     * 开始网络访问
     */
    private void start() {
        isRequesting = true;

        callable = x.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onSuccess(String result) {
                onReturnSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (act != null && act.isForeground()) {
                    String resultInfo = failInfo;
                    Throwable throwable = ex.getCause();
                    if (throwable != null) {
                        if (throwable instanceof ConnectException) { //找不到主机错误
                            resultInfo = act.getIdString(R.string.net_connect_fail);
                        } else if (throwable instanceof ConnectTimeoutException) {//连接超时
                            resultInfo = act.getIdString(R.string.net_socket_timeout);
                        } else if (throwable instanceof SocketTimeoutException) { //数据传输超时
                            resultInfo = act.getIdString(R.string.net_connect_fail);
                        }
                    }
                    stopNetWork();
                    act.showToast(resultInfo);

                    back.onPostcAsync(status, new BackMsg(resultInfo, ""), false);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (act != null && act.isForeground()) {
                    act.showToast("联网被取消了");
                }
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if (isNeedProgressDialog && act.isForeground())
                    act.showProgressDialog(waitInfo);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }
        });
    }


    /**
     * 停止网络请求访问
     */
    public void stopNetWork() {
        isRequesting = false;
        act.dialogCancel();
        callable.cancel();
    }

    /**
     * 处理请求成功 返回的数据
     */
    protected void onReturnSuccess(String response) {
        if (isRequesting) {
            BackMsg msg = new BackMsg(failInfo);
            String result = response;
            XL.e("onSuccess访问成功了()" + result);
            if (result != null) {
                try {
                    JSONObject obj = new JSONObject(result);
                    msg = getReturnMsg(obj);
                    JSONObject jsonObj = new JSONObject();
                    if (backHeads != null && !backHeads.isEmpty()) {
                        for (Iterator<String> it = backHeads.iterator(); it.hasNext(); ) {
                            String key = it.next();
//                            Header header = response.getFirstHeader(key);
//                            if (header != null)
//                                jsonObj.put(key, header.getValue());
                        }
                    }
                    obj.put("head", jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pro_result(msg);
        }
    }

    /**
     * 从结果中解析出返回数据内容
     * 重写获取方法BackMsg方法,这里可对每一个应用不同操作
     *
     * @param obj
     * @return
     * @throws JSONException
     */
    protected BackMsg getReturnMsg(JSONObject obj) throws JSONException {
        return new BackMsg(obj.getString("Code"), obj.getString("Message"), obj.getInt("Code") == 0, obj);
    }

    /**
     * 网络获取结束 调用方法 ,处理结果
     */
    public void pro_result(final BackMsg msg) {
        boolean isSuccess = msg.isSuccess();
        if (!isSuccess) {
            act.showToast(msg.getRespMsg());
        }
        back.onPostcAsync(status, msg, isSuccess);
        stopNetWork();
    }

    /**
     * 登陆超时则回调该方法
     */
    public abstract void loginTimeOut();

}
