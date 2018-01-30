package com.blazers.jandan.ui.activity.base

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import butterknife.ButterKnife

/**
 * Created by blazers on 2018/1/30.
 */

@SuppressLint("Registered")
open abstract class BaseActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var toolbarWithShadow: ViewGroup? = null

    /**
     * 设置StatusBar显示的ICON为深色ICON
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun setStatusBarLightTheme() {
        if (window != null && window.decorView != null) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }

    /**
     * 取消StatusBar的Tint模式并显示黑色
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun disableStatusBarTheme() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.BLACK
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // M 以上的版本会自动使用style里面的配置设置Tint
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            disableStatusBarTheme()
        }
    }

    /**
     * 自动Bind
     *
     * @param layoutResID 布局资源
     */
    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(getLayoutResId())
        ButterKnife.bind(this)
    }

    /**
     * 设置视图的浮动模式 设置内容能够显示在status bar navigation bar 之后
     */
    fun setReadyForImmersiveMode() {
        window.decorView.systemUiVisibility = getUiOptionFlagForReadyImmersiveMode()
        // 设置toolbar 的 padding
        if (toolbar != null) {
            toolbar?.setPadding(toolbar?.getPaddingLeft(),
                    getStatusBarHeight() + toolbar?.getPaddingTop(),
                    toolbar?.getPaddingRight(),
                    toolbar?.getPaddingBottom()))
        }
    }

    /**
     * TODO
     */
    fun setNormalWindow() {
        var uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 内容浮动在Status bar 之后

                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE // 真 沉浸模式
        }
        window.decorView.systemUiVisibility = uiOptions
    }

    /**
     * TODO
     */
    fun showSystemUI(toolbar: Toolbar) {
        window.decorView.systemUiVisibility = getUiOptionFlagForReadyImmersiveMode()
        // handle layout_toolbar
        val animate = if (toolbarWithShadow == null) toolbar else toolbarWithShadow
        animate?.animate()?.translationY(0f)?.setDuration(150)?.start()
    }

    /**
     * TODO
     */
    fun hideSystemUI(toolbar: Toolbar) {
        window.decorView.systemUiVisibility = getUiOptionFlagForGoingToImmersiveMode()
        // handle layout_toolbar
        val animate = if (toolbarWithShadow == null) toolbar else toolbarWithShadow
        animate?.animate()?.translationY((-toolbar.height).toFloat())?.setDuration(400)?.setStartDelay(200)?.start()
    }


    /**
     * 获取StatusBar 高度
     */
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取导航条高度
     */
    fun getNavigationBarHeight(): Int {
        val resIdShow = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        var hasNavigationBar = false
        if (resIdShow > 0) {
            hasNavigationBar = resources.getBoolean(resIdShow)//是否显示底部navigationBar
        }
        if (hasNavigationBar) {
            val resIdNavigationBar = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val navigationBarHeight: Int
            if (resIdNavigationBar > 0) {
                navigationBarHeight = resources.getDimensionPixelSize(resIdNavigationBar)//navigationBar高度
                return navigationBarHeight
            }
        }
        return 0
    }

    private fun getUiOptionFlagForReadyImmersiveMode(): Int {
        var uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 使内容能够显示在在 Status bar 之后

                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE     // 同上

                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)   // 让内容能够显示在 Navigation bar 之后
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        return uiOptions
    }

    private fun getUiOptionFlagForGoingToImmersiveMode(): Int {
        var uiOptions = (View.SYSTEM_UI_FLAG_FULLSCREEN // 隐藏Status bar 与 Navigation bar
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // 使内容能够显示在在 Status bar 之后
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE     // 同上
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION           // 隐藏 Navigation bar
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)   // 让内容能够显示在 Navigation bar 之后
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE // 真 沉浸模式
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        return uiOptions
    }

    abstract fun getLayoutResId() : Int
}
