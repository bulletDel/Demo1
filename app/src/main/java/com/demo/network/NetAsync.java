package com.demo.network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.activity.LoginActivity;
import com.demo.app.DemoApplication;
import com.demo.bean.BackMsg;
import com.demo.bean.DownloaderAction;
import com.demo.listener.BaseActivityListener;
import com.demo.listener.XHttpCallBack;
import com.demo.service.DownloadService;
import com.demo.util.XL;
import com.demo.util.XPreferencesService;
import com.demo.util.XSystemUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;

public class NetAsync extends XNetAsync {

    public NetAsync(BaseActivityListener listener, XHttpCallBack back, int waitResId, int failResId, int status) {
        super(listener, back, waitResId, failResId, status);
    }


    public NetAsync(BaseActivityListener listener, XHttpCallBack back, int waitResId, int failResId, int status, boolean isNeedProgressDialog) {
        super(listener, back, waitResId, failResId, status, isNeedProgressDialog);
    }


    //  public NetAsync(BaseActivityListener listener, XHttpCallBack back, int waitResId, int failResId, int status, boolean isNeedProgressDialog, boolean isCancelable, DialogInterface.OnCancelListener cancelListener) {
    //    super(listener, back, waitResId, failResId, status, isNeedProgressDialog, isCancelable, cancelListener);
    //  }

    @Override
    public void send(String url, RequestParams params) {
        this.send(url, params, null);
    }

    @Override
    public void send(String url, RequestParams params, int status) {
        this.status = status;
        this.send(url, params, null);
    }

    @Override
    public void send(String url, RequestParams params, ArrayList<String> backHeads) {
        if (params == null) {
            params = new RequestParams(url);
        }
        ArrayList<KeyValue> values = (ArrayList<KeyValue>) params.getStringParams();
        XL.e("============");
        StringBuilder stringBuilder = new StringBuilder(url + "?");
        for (int i = 0; i < values.size(); i++) {
            KeyValue value = values.get(i);
            XL.e("参数：" + value.key + ":" + value.getValueStr());
            stringBuilder.append(value.key + "=" + value.getValueStr());
            if (i < values.size() - 1) {
                stringBuilder.append("&");
            }
        }
        XL.e("============");
        
        Context ctx = act.getContext();
        XPreferencesService service = DemoApplication.getInstance().getPreService();

        super.send(url, params, backHeads);
    }


    @Override
    public void loginTimeOut() {
        if (act != null) {
            act.appCancel(act);
            act.qStartActivity(new Intent(act.getContext(), LoginActivity.class));
            act.activityFinish();
        }
    }

    @Override
    protected BackMsg getReturnMsg(JSONObject obj) throws JSONException {
        BackMsg msg = new BackMsg(obj.getInt("Code") == 0, obj);
        if (obj.has("Message")) {
            msg.setRespMsg(obj.getString("Message"));
        }
        if (obj.has("Code")) {
            msg.setRespCode(obj.getString("Code"));
        }
        return msg;
    }

    /**
     * 网络获取结束 调用方法 ,处理结果
     */
    @Override
    public void pro_result(BackMsg msg) {
        if (msg != null && (msg.getRespCode().equals("2") || msg.getRespCode().equals("9009"))) {
            loginTimeOut();
        } else if (msg.getRespCode().equals("9029")) {//版本过低,强制更新
            try {
                JSONObject obj = new JSONObject(msg.getRespMsg());
                String newVersion = obj.getString("version"); //获取最新版本号
                String oldVersion = XSystemUtil.getVersionName(act.getContext()); //获取当前版本号
                OnReceiveBroadcase(oldVersion, newVersion, obj.getString("address"), obj.getString("msg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            stopNetWork();
            //      loginTimeOut();
            //      back.onPostcAsync(status, msg, msg.isSuccess());
        } else {
            super.pro_result(msg);
        }
    }

    public void OnReceiveBroadcase(final String oldVersion, final String newVersion, final String address, String title) {
        String saveDir = DemoApplication.getInstance().getFilesDir().getAbsolutePath();
        //        if (BaseApplication.getInstance() != null) {
        //            saveDir = BaseApplication.getInstance().getSaveDir();
        //        }
        final Context context = act.getContext();
        final String dir = saveDir;
        final String fileName = context.getString(R.string.company) + newVersion + ".apk";
        File localFile = new File(dir + fileName);
        if (localFile != null && localFile.exists() && localFile.isFile()) {// +  name
            //如果文件已经下载过,则直接提示安装
            XSystemUtil.Instanll(context, localFile);
        } else {
            final String appName = context.getString(R.string.app_name) + " ";
            String message = "当前版本为" + oldVersion + " ,是否更新为最新版本" + newVersion;
            if (dialog == null || !dialog.isShowing()) {
                dialog = new AlertDialog.Builder(context).setTitle(title).setMessage(appName + message).setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //                        Intent intent = new Intent(context.getString(R.string.action_downloader));
                        //                        Bundle data = new Bundle();
                        //                        data.putSerializable(DownloaderService.DOWNLOAD_URL, new DownloaderAction(address, dir, fileName, MsgNotifyFragmentActivity.class));
                        //                        intent.putExtras(data);
                        if (PreferenceManager.getDefaultSharedPreferences(context).getString("firstDown", null) == null) {
                            Intent intent = new Intent(context, DownloadService.class);
                            intent.putExtra(DownloadService.DOWNLOAD_URL, new DownloaderAction(address, dir, fileName, MainActivity.class));
                            context.startService(intent);
                            PreferenceManager.getDefaultSharedPreferences(context).edit().putString("firstDown", "first").commit();
                            act.showToast("开始下载新版本...");
                        } else {
                            act.showToast("已经开始下载!");
                        }
                    }
                }).setNegativeButton(R.string.dialog_cannel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DemoApplication.getInstance().appCancel(null);
                    }
                }).create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);
                dialog.show();
            }
        }
    }

    Dialog dialog;
}
