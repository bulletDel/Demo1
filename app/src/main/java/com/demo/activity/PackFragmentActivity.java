package com.demo.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demo.R;

public class PackFragmentActivity extends CompontUtilFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initConstants();
    }

    protected ImageButton ibLeft, ibRight;
    protected TextView tvLeft, tvRight;
    protected TextView tvTitle;
    protected RelativeLayout rlTitle;


    protected void initView() {
        if (findViewById(R.id.ibLeft) != null) {
            ibLeft = (ImageButton) findViewById(R.id.ibLeft);
        }
        if (findViewById(R.id.ibRight) != null) {
            ibRight = (ImageButton) findViewById(R.id.ibRight);
        }

        if (findViewById(R.id.tvTitle) != null) {
            tvTitle = (TextView) findViewById(R.id.tvTitle);
        }
        if (findViewById(R.id.rlTitle) != null) {
            rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        }

        if (findViewById(R.id.tvLeft) != null) {
            tvLeft = (TextView) findViewById(R.id.tvLeft);
        }
        if (findViewById(R.id.tvRight) != null) {
            tvRight = (TextView) findViewById(R.id.tvRight);
        }
    }

    protected void initListener() {

        // 点击事件可以在子类中被覆盖
        if (ibLeft != null) {
            ibLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }

        // 点击事件可以在子类中被覆盖
        if (tvLeft != null) {
            tvLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }

        if (ibRight != null) ibRight.setOnClickListener(this);
        if (tvRight != null) tvRight.setOnClickListener(this);

    }

    protected void getData() {
    }

    protected void initData() {
    }

    protected void setCustomTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    protected void hideLeftImage() {
        ibLeft.setVisibility(View.INVISIBLE);
    }

    protected void showLeftImage(int resID) {
        ibLeft.setImageResource(resID);
        ibLeft.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.INVISIBLE);
    }

    protected void showLeftText(String text) {
        tvLeft.setText(text);
        tvLeft.setVisibility(View.VISIBLE);
        ibLeft.setVisibility(View.INVISIBLE);
    }

    protected void showRightText(String text) {
        tvRight.setText(text);
        tvRight.setVisibility(View.VISIBLE);
        ibRight.setVisibility(View.INVISIBLE);
    }

    protected void showRightImage(int resID) {
        ibRight.setImageResource(resID);
        ibRight.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.INVISIBLE);
    }

    protected Activity activity;
    protected int screenWidth, screenHeight, _1dp, _5dp, _10dp, _15dp, _44dp, _100dp, _10sp, _12sp, _13sp, _14sp, _15sp, _16sp;

    private void initConstants() {
        activity = this;

        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        _5dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        _10dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        _1dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
        _15dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        _44dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 44, getResources().getDisplayMetrics());
        _100dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        _10sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
        _12sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        _13sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, getResources().getDisplayMetrics());
        _14sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());
        _15sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15, getResources().getDisplayMetrics());
        _16sp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics());
    }

}
