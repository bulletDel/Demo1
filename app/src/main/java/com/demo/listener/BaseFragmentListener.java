package com.demo.listener;


import com.demo.base.BaseFragment;

/**
 * 带有父子容器Activity活动的，子容器获取回调监听器
 *
 * @author xmq
 */
public interface BaseFragmentListener {
    /**
     * 设置当前BackHandledFragment 为显示的Fragment
     *
     * @param subFragment 在前台显示的Fragment
     */
    void addSubFragment(BaseFragment subFragment);

    /**
     * 从当前Activity中移除掉当前BackHandledFragment
     *
     * @param subFragment 在前台显示的Fragment
     */
    void removeSubFragment(BaseFragment subFragment);

    /**
     * startActivityForResult 回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    //	void onBackResult(int requestCode, int resultCode, Intent data);
}
