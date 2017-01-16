package com.demo.listener;

import android.view.KeyEvent;
import android.view.View;

/**
 * 针对父容器FragmentActivity有的控件，在各子Fragment显示时的操作的回调接口
 * <code>各子Fragment实现该接口，回调父FragmentActivity</code>
 */
public interface FragActViewListener {
    /**
     * FragmentActivity中的view被点击回调方法
     *
     * @param resViewId 点击的view的id
     * @param resView   点击的View
     */
    void onFragActViewClick(int resViewId, View resView);

    /**
     * FragmentActivity中的硬件键盘点击事件
     *
     * @param keyCode 点击的按钮code
     * @param event   点击的事件event
     * @return 如果返回false, 则会拦截 父FragmentActivity的 键盘点击事件
     */
    boolean onKeyDown(int keyCode, KeyEvent event);

}