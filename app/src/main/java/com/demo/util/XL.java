package com.demo.util;

import android.util.Log;

import com.demo.app.BaseApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public final class XL {
	private final static String TAG = "xu";

	public final static void d(String info) {
		if (BaseApplication.isDebug)
			Log.d(TAG, info);
	}

	public final static void i(String info) {
		if (BaseApplication.isDebug)
			Log.i(TAG, info);
	}

	public final static void w(String info) {
		if (BaseApplication.isDebug)
			Log.w(TAG, info);
	}

	public final static void e(String info) {
		if (BaseApplication.isDebug)
			Log.e(TAG, info);
	}

	public static HashSet<String> tags;
	static {
		tags = new HashSet<String>();
		// tags.add("at android.");
		// tags.add("at java.");
		tags.add("at com.android.");
		tags.add("at com.qihoo360.");
		tags.add("at dalvik.");
	}

	/** 将异常错误日志写入到指定log文件 */
	public final static void writeLog(String path) {
		ArrayList<String> commandLine = new ArrayList<String>();
		commandLine.add("logcat");
		commandLine.add("-d");
		commandLine.add("-v");
		commandLine.add("time");
		commandLine.add("-s");
		commandLine.add("*:W");
		// commandLine.add("xu:d");
		Log.i("xu", "开始获取日志");
		try {
			Process process = Runtime.getRuntime().exec(
					commandLine.toArray(new String[commandLine.size()]));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()), 4096);
			StringBuilder log = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				// for (String str : tags) {
				// if (line.contains(str))
				// continue;
				// }
				Log.d("xu", "\t日志：" + line);
				log.append(line + "\n");
			}
			Log.i("xu", "开始获取日志2");
			File dir = new File(path + "/log/");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			OutputStream ops = new FileOutputStream(new File(dir,
					new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
							.format(new Date()) + ".txt"));
			ops.write(log.toString().getBytes());
			ops.flush();
			ops.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
