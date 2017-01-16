package com.demo.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.demo.R;


/**
 * 信息提示窗口
 */
public class XProgressDialog extends Dialog {
    private Animation anim;
    TextView tipTextView;
    Window window;

    public XProgressDialog(Context context, int theme) {
        super(context, theme);
        anim = AnimationUtils.loadAnimation(context, R.anim.load_anim);
    }

    public void showDialog(String msg) {
        //当前 界面为空，或者 当前dialog 设置 hide（）了
        if (window == null
                || window.getDecorView().getVisibility() == View.VISIBLE) {
            setContentView(R.layout.dialog_loading);
            windowDeploy();
            findviews();
        }
        if (!anim.hasStarted()) {
            findViewById(R.id.img).startAnimation(anim);
        }
        tipTextView.setText(msg);// 设置加载信息
        show();
    }

    @Override
    public void cancel() {
        if (anim != null && anim.hasStarted()) {
            anim.cancel();
        }
        super.cancel();
    }

    @Override
    public void dismiss() {
        if (anim != null && anim.hasStarted()) {
            anim.reset();
        }
        super.dismiss();
    }

    private void findviews() {
        tipTextView = (TextView) findViewById(R.id.tipTextView);
    }

    // 设置窗口显示
    public void windowDeploy() {
        // 设置触摸对话框意外的地方取消对话框
        setCanceledOnTouchOutside(false);
        window = getWindow(); // 得到对话框
        // window.setWindowAnimations(R.style.goldDialogWindowAnim); // 设置窗口弹出动画
        // window.setBackgroundDrawableResource(R.drawable.bluebtn); //
        // 设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        // wl.alpha = 0.9f; //设置透明度
        wl.gravity = Gravity.CENTER; // 设置重力
        window.setAttributes(wl);
    }

    @Override
    protected void onStop() {
        super.onStop();
        window = null;
    }
}