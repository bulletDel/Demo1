package com.demo.listener;


import com.demo.bean.BackMsg;

/**
 * 自定义Activity 网络请求监听回调接口类
 */
public interface XHttpCallBack {


  /**
   * 网络异步任务，执行后回调方法
   *
   * @param status    执行网络访问时，设置的状态。 用于区分在同一个Activity中，当前执行的是哪一步网络请求
   * @param responMsg 封装的网络结果返回。
   * @param isSuccess 请求结果是否成功
   */
  public void onPostcAsync(int status, BackMsg responMsg, boolean isSuccess) ;

}
