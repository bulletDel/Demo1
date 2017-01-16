package com.demo.listener;

import android.os.Bundle;

import com.demo.base.BaseFragment;


/**
 * 父FragmentActivity容器操作回调接口
 *
 * @author xmq
 */
public interface FragmentActivityListener {

    /**
     * 设置父容器FragmentActivity里的View点击监听器
     * <p>一般为子Fragment调用设置监听父Activity的控件点击</p>
     *
     * @param listener 监听器
     */
    void setFragActViewListener(FragActViewListener listener);

    /**
     * 移除指定Fragment回调方法
     * 要研究下FragmentManager的管理模式.此方法暂时不可用
     *
     * @param fragment 需移除的fragment的class
     */
    @Deprecated
    void onRemoveFragment(Class<? extends BaseFragment> fragment);

    /**
     * 跳转下一个Fragment回调方法
     *
     * @param viewId   进行显示在的父容器id
     * @param fragment 需跳转到的fragment的class
     */
    void onNextFragment(int viewId, Class<? extends BaseFragment> fragment);

    /**
     * 跳转下一个Fragment回调方法
     *
     * @param viewId   进行显示在的父容器id
     * @param fragment 需跳转到的fragment的class
     * @param data     传递的参数
     */
    void onNextFragment(int viewId, Class<? extends BaseFragment> fragment, Bundle data);

    /**
     * 返回上一个Fragment回调方法
     */
    void onLastFragment();

}
