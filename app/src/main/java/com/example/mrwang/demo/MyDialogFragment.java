package com.example.mrwang.demo;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by MrWang on 2018/5/12.
 */

public class MyDialogFragment extends DialogFragment {
    private View mView;
    private TextView mTopTextView;
    private TextView mBottomTextView;
    private TextView mCancel;
    private View mDecorView;
    private Animation mIntoSlide;
    private Animation mOutSlide;
    private boolean isClick = false;//过滤重复点击
    public DialogCallback diglogCallback;

    /**
     * 通讯回调接口
     */
    public interface DialogCallback {
        //打开相册
        void openPhoto();

        //打开相机拍照
        void openCamera();
    }

    public void setDialogCallback(DialogCallback diglogCallback) {
        this.diglogCallback = diglogCallback;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_dialog, container, false);
        mCancel = mView.findViewById(R.id.btn_cancle);
        mTopTextView = mView.findViewById(R.id.text_bg_top);
        mBottomTextView = mView.findViewById(R.id.text_bg_bottom);
        //初始化Dialog
        initDialogView();
        return mView;
    }

    /**
     * 根据业务需求，更改弹窗的一些样式
     */
    public void initDialogView() {
        mDecorView = getDialog().getWindow().getDecorView();
        //设置背景为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDecorView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //居屏幕底部
        layoutParams.gravity = Gravity.BOTTOM;
        //给window宽度设置成填充父窗体，解决窗体宽度过小问题
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(layoutParams);
        //mDecorView.setPadding(0, 0, 0, 0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //弹窗弹入屏幕的动画
        initIntAnimation();
        //初始化监听
        initListener();
        //手指点击弹窗外处理
        touchOutShowDialog();
        //back键处理
        getFocus();


    }

    public void initListener() {
        /**
         * "取消"条目的点击事件
         * */
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //执行关闭的动画
                dimissDialog();
                //将其他控件内条目设置成不可以点击的状态
                mTopTextView.setClickable(false);
                mBottomTextView.setClickable(false);
            }
        });
        /**
         * "打开相机"条目的点击事件
         * */
        mTopTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //执行打开相机的回调方法
                diglogCallback.openCamera();
                //关闭弹窗
                dimissDialog();
            }
        });
        /**
         * "打开相册"条目的点击事件
         * */
        mBottomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //执行打开相机的回调方法
                diglogCallback.openPhoto();
                //关闭弹窗
                dimissDialog();
            }
        });

    }

    /**
     * 弹窗弹入屏幕的动画
     */
    private void initIntAnimation() {
        if (mIntoSlide != null) {
            mIntoSlide.cancel();
        }
        mIntoSlide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        mIntoSlide.setDuration(200);
        mIntoSlide.setFillAfter(true);
        mIntoSlide.setFillEnabled(true);
        mView.startAnimation(mIntoSlide);
    }

    /**
     * 过滤重复点击
     */
    public void dimissDialog() {
        if (isClick) {
            return;
        }
        isClick = true;
        initOutAnimation();
    }

    /**
     * 弹窗弹出屏幕的动画
     */
    private void initOutAnimation() {
        if (mOutSlide != null) {
            mOutSlide.cancel();
        }
        mOutSlide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 1.0f);
        mOutSlide.setDuration(200);
        mOutSlide.setFillEnabled(true);
        mOutSlide.setFillAfter(true);
        mView.startAnimation(mOutSlide);
        /**
         * 弹出屏幕动画的监听
         */
        mOutSlide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //过滤重复点击的标记
                isClick = false;
                //销毁弹窗
                MyDialogFragment.this.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

    }

    /**
     * 拦截手势(弹窗外区域)
     */
    public void touchOutShowDialog() {
        mDecorView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //弹框消失的动画执行相关代码
                    dimissDialog();
                }
                return true;
            }
        });
    }

    /**
     * 监听主界面back键
     * 当点击back键时，执行弹窗动画
     */
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 监听到back键(悬浮手势)返回按钮点击事件
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    //判断弹窗是否显示
                    if (MyDialogFragment.this.getDialog().isShowing()) {
                        //关闭弹窗
                        dimissDialog();
                        return true;
                    }
                }
                return false;
            }
        });
    }


}
