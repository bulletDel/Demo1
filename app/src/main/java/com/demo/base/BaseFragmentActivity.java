package com.demo.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.demo.listener.FragActViewListener;


/**
 * 抽象的FragmentActivity
 */
public abstract class BaseFragmentActivity extends AbstractBaseFragmentActivity
        implements View.OnClickListener {
    FragActViewListener listener; //子Fragment的一系列回调监听器

    @Override
    public void addSubFragment(BaseFragment subFragment) {
        subList.put(subFragment.getId(), subFragment);
    }

    @Override
    public void removeSubFragment(BaseFragment subFragment) {
        subList.remove(subFragment.getId());
    }

    /**
     * 设置父容器FragmentActivity里的View点击监听器
     * <p>一般为子Fragment调用设置监听父Activity的控件点击</p>
     */
    @Override
    public void setFragActViewListener(FragActViewListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        if (listener != null) {
            listener.onFragActViewClick(id, view);
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag;
        if (listener != null && (flag = listener.onKeyDown(keyCode, event)))
            return flag;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRemoveFragment(Class<? extends BaseFragment> fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.commit();
    }

    @Override
    public void onNextFragment(int viewId, Class<? extends BaseFragment> fragment) {
        Intent intent = getIntent();
        Bundle data = null;
        if (intent != null) {
            data = intent.getExtras();
        }
        onNextFragment(viewId, fragment, data);
    }

    @Override
    public void onNextFragment(int viewId, Class<? extends BaseFragment> fragment, Bundle data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BaseFragment frag = null;
        try {
            frag = fragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (frag == null) {
            Log.e("xu", "This Fragment is null when fragments get Key " + fragment);
            return;
        }
        if (data != null)
            frag.setArguments(data);
        transaction.replace(viewId, frag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLastFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            activityFinish();
        }
    }


}