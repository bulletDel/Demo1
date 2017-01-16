package com.demo.listener;


import com.demo.util.XPreferencesService;

/**
 * 应用资源工厂接口
 */
public interface AppInterface {

	/** 得到参数保存SharedPreference */
	XPreferencesService getPreService();

	/** 得到手机当前安装版本信息 */
	String getVersion();

	/** 获取皮肤保存路径 */
	String getSkinSaveDir();

	/** 获取缓存数据保存路径 */
	String getAppCacheDir();

	/** 获取应用数据路径 */
	String getDataDir1();

	/** 获取软件根保存路径,默认为 手机sd卡根保存目录 */
	String getSaveDir();

}