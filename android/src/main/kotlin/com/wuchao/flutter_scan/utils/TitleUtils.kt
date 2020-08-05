package com.wuchao.flutter_scan.utils

import android.app.Activity
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity


/**
 * 隐藏状态栏
 *  View.SYSTEM_UI_FLAG_FULLSCREEN 隐藏状态栏
 *  此方法会导致界面抖动
 */
fun hideStatusBar(context: AppCompatActivity) {
    context.window.apply {
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_IMMERSIVE
    }
}

/**
 * 隐藏导航栏
 *  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 隐藏导航栏
 */
fun hideNavigationBar(context: AppCompatActivity) {
    context.window.apply {
        decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE
    }
}


/**
 * 隐藏系统栏
 * View.SYSTEM_UI_FLAG_FULLSCREEN 隐藏状态栏
 *  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION 隐藏导航栏
 */
fun hideSystemBar(context: AppCompatActivity) =
        Unit.apply {
            context.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_IMMERSIVE
        }

/**
 * 悬浮状态栏
 */
fun setSuspendStatusBar(context: AppCompatActivity) =
        Unit.apply {
            context.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

/**
 * 悬浮导航栏
 */
fun setSuspendNavigationBar(context: AppCompatActivity) =
        Unit.apply {
            context.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

/**
 * 悬浮系统栏
 */
fun setSuspendSystemBar(context: AppCompatActivity) =
        Unit.apply {
            context.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }


/**
 * 隐藏标题栏
 */
fun hideActionBar(context: AppCompatActivity) = Unit.apply { context.supportActionBar?.hide() }

/**
 * 显示标题栏
 */
fun showActionBar(context: AppCompatActivity) = Unit.apply { context.supportActionBar?.show() }


/**
 * 设置状态栏颜色
 */
fun setStatusBarColor(context: AppCompatActivity, color: Int) =
        Unit.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                context.window.statusBarColor = color
            }
        }

fun setStatusBarLightColor(activity: Activity, @ColorInt color: Int) {
    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity.window.statusBarColor = color
    }
}
