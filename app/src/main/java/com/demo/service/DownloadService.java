package com.demo.service;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.demo.bean.DownloaderAction;
import com.demo.util.XL;
import com.demo.util.XSystemUtil;

import java.io.File;

/**
 * 用来下载的service
 * <p/>
 * Created by dmz on 2015/10/14.
 */
public class DownloadService extends Service {

    private DownloaderAction downloaderAction;//用来接收文件相关信息

    public static final String DOWNLOAD_URL = "down_load_url";//传递下载url的key

    private Receiver receiver;//用来接收下载返回的状态

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            downloaderAction = (DownloaderAction) intent.getSerializableExtra(DOWNLOAD_URL);
        }

        if (downloaderAction != null) {
            DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloaderAction.url));

            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();//获取文件类型实例
            String mimeStr = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloaderAction.url));//获取文件类型
            request.setMimeType(mimeStr);

            //request.setDestinationInExternalPublicDir(downloaderAction.savePath, downloaderAction.saveName);//设置下载目录

            long id = manager.enqueue(request);

            PreferenceManager.getDefaultSharedPreferences(this).edit().putLong("downloadId", id).commit();//保存下载ID
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        registerReceiver(receiver, filter);
    }

    private class Receiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //下载完成的监听
                Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                long oldId = PreferenceManager.getDefaultSharedPreferences(context).getLong("downloadId", -1);
                if (id == oldId) {
                    //判断是不是同一个下载任务
                    XL.e("这是同一个下载任务");
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString("firstDown", null).commit();

                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    Cursor cursor = manager.query(query);
                    int columnCount = cursor.getColumnCount();//得到的所有的列
                    String path = null;
                    while (cursor.moveToNext()) {
                        for (int j = 0; j < columnCount; j++) {
                            String columnName = cursor.getColumnName(j);
                            String string = cursor.getString(j);

                            if (string != null) {
                                XL.e(columnName + ": " + string);
                            } else {
                                XL.e(columnName + ": null");
                            }


                            if (columnName.equals("local_filename")) {
                                path = string;
                                break;
                            }
                        }
                        if (path != null) {
                            break;
                        }
                    }
                    cursor.close();

                    if (path != null) {
                        File localFile = new File(path);
                        if (localFile != null && localFile.exists() && localFile.isFile()) {// +  name
                            XSystemUtil.Instanll(context, localFile);
                        }
                    }

                }
            } else if (action != null && action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                //通知的点击事件

            }

        }
    }

}
