package com.blazers.jandan.views;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blazers.jandan.R;
import com.blazers.jandan.util.Dppx;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Blazers on 2016/1/8.
 */
public class PopupActionModeBar {

    // Sub views
    @BindView(R.id.icon)
    ImageView mIcon;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.delete)
    ImageView mDelete;
    private PopupWindow mPopupWindow;
    private int mCount = 1;

    public PopupActionModeBar(Activity context, ActionbarCallback mCallback) {
        // 载入布局
        View view = LayoutInflater.from(context).inflate(R.layout.popup_action_mode_bar, null);
        ButterKnife.bind(this, view);
        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(Dppx.Dp2Px(context, 56));
        mPopupWindow.setContentView(view);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable()); // 用于去除默认的阴影效果
        mPopupWindow.setAnimationStyle(R.style.ActionModeAnima);
        mPopupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, 0);
        // Init Animation
        mIcon.animate().scaleX(1).scaleY(1).setDuration(200).start();
        mTitle.setText(String.format("已选择 %d", mCount));
        mTitle.animate().translationX(0).alpha(1).setStartDelay(200).setDuration(700).withStartAction(() -> {
            mTitle.setTranslationX(-112);
            mTitle.setAlpha(0);
        }).start();
        mDelete.animate().rotationX(0).setStartDelay(200).setDuration(500).start();

        // Bind
        mIcon.setOnClickListener(v -> {
            mPopupWindow.dismiss();
            mCallback.onBackPressed();
        });
        mDelete.setOnClickListener(v -> {
            mPopupWindow.dismiss();
            mCallback.onDeletePress();
        });
        view.findViewById(R.id.wrapper).setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                mPopupWindow.dismiss();
                mCallback.onBackPressed();
                return true;
            }
            return false;
        });
    }

    public void addSelection() {
        mTitle.setText(String.format("已选择 %d", ++mCount));
    }

    public void minusSelection() {
        if (mCount <= 0)
            return;
        if (mCount == 1) {
            mCount = 0;
            mTitle.animate().rotationX(180).setDuration(300).withEndAction(() -> {
                mTitle.setAlpha(0);
                mTitle.setRotationX(0);
            }).start();
        } else {
            mTitle.setText(String.format("已选择 %d", --mCount));
        }
    }

    public interface ActionbarCallback {
        void onBackPressed();

        void onDeletePress();
    }
}
