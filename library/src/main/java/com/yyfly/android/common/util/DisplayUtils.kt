package com.yyfly.android.common.util

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.Surface
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.yyfly.android.common.App

import java.lang.reflect.Field


/**
 * 显示工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object DisplayUtils {

    /**
     * The constant SCREEN_WIDTH_PIXELS.
     */
    var SCREEN_WIDTH_PIXELS: Int = 0
    /**
     * The constant SCREEN_HEIGHT_PIXELS.
     */
    var SCREEN_HEIGHT_PIXELS: Int = 0

    var SCREEN_DENSITY_DPI: Int = 0
    /**
     * The constant SCREEN_DENSITY.
     */
    var SCREEN_DENSITY: Float = 0f
    /**
     * The constant SCREEN_WIDTH_DP.
     */
    var SCREEN_WIDTH_DP: Int = 0
    /**
     * The constant SCREEN_HEIGHT_DP.
     */
    var SCREEN_HEIGHT_DP: Int = 0

    var sInitialed: Boolean = false

    /**
     * 初始属性
     *
     * @param context the context
     */
    fun init(context: Context?) {
        if (sInitialed || context == null) {
            return
        }
        sInitialed = true
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(dm)
        SCREEN_WIDTH_PIXELS = dm.widthPixels
        SCREEN_HEIGHT_PIXELS = dm.heightPixels
        SCREEN_DENSITY = dm.density
        SCREEN_DENSITY_DPI = dm.densityDpi
        SCREEN_WIDTH_DP = (SCREEN_WIDTH_PIXELS / dm.density).toInt()
        SCREEN_HEIGHT_DP = (SCREEN_HEIGHT_PIXELS / dm.density).toInt()
    }

    /**
     * 获取屏幕的宽度（单位：px）
     * @return 屏幕宽px
     */
    val screenWidth: Int
        get() {
            val windowManager = App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }

    /**
     * 获取屏幕的高度（单位：px）
     * @return 屏幕高px
     */
    val screenHeight: Int
        get() {
            val windowManager = App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.heightPixels
        }
    /**
     * 判断是否横屏

     * @return `true`: 是<br></br>`false`: 否
     */
    val isLandscape: Boolean
        get() = App.context.getResources().getConfiguration().orientation === Configuration.ORIENTATION_LANDSCAPE

    /**
     * 判断是否竖屏

     * @return `true`: 是<br></br>`false`: 否
     */
    val isPortrait: Boolean
        get() = App.context.getResources().getConfiguration().orientation === Configuration.ORIENTATION_PORTRAIT

    /**
     * 判断是否锁屏
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isScreenLock: Boolean
        get() {
            val km = App.context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            return km.inKeyguardRestrictedInputMode()
        }

    /**
     * 获取进入休眠时长,进入休眠时长，报错返回-123
     * 设置进入休眠时长
     * 需添加权限 `<uses-permission android:name="android.permission.WRITE_SETTINGS" />`
     */
    var sleepDuration: Int
        get() {
            try {
                return Settings.System.getInt(App.context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT)
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
                return -123
            }
        }
        /**
         *  @param duration 时长
         */
        set(duration) {
            Settings.System.putInt(App.context.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, duration)
        }

    /**
     * dp转像素

     * @param dp the dp
     * *
     * @return int
     */
    fun dp2px(dp: Float): Int {
        val scale = SCREEN_DENSITY
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * px 转 dp
     *
     * @param pxValue the px value
     * @return int
     */
    fun px2dp(pxValue: Float): Int {
        return (pxValue / SCREEN_DENSITY + 0.5f).toInt()
    }

    /**
     * px转sp
     * @param context the context
     * @param pxValue the px value
     * @return int
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        return (pxValue / SCREEN_DENSITY + 0.5f).toInt()
    }

    /**
     * Px 2 sp int.
     *
     * @param pxValue the px value
     * @return the int
     */
    fun px2sp(pxValue: Float): Int {
        return (pxValue / SCREEN_DENSITY + 0.5f).toInt()
    }

    /**
     * Sp 2 px int.
     *
     * @param spValue the sp value
     * @return the int
     */
    fun sp2px(spValue: Float): Int {
        return (spValue * SCREEN_DENSITY + 0.5f).toInt()
    }

    /**
     * This snippet hides the system bars.
     *
     * @param activity the activity
     * @author : mingweigao / 2016-07-06
     */
    fun hideSystemUI(activity: Activity) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        // hide nav bar
        var uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags = uiFlags or 0x00001000    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags = uiFlags or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        activity.window.decorView.systemUiVisibility = uiFlags
    }

    /**
     * @param window
     */
    fun hideSystemUI(window: Window) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        var uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags = uiFlags or 0x00001000    //SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide navigation bars - compatibility: building API level is lower thatn 19, use magic number directly for higher API target level
        } else {
            uiFlags = uiFlags or View.SYSTEM_UI_FLAG_LOW_PROFILE
        }
        window.decorView.systemUiVisibility = uiFlags
    }

    /**
     * 获取 StatusBar 高度
     *
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        var sbar = 0
        try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c!!.newInstance()
            field = c.getField("status_bar_height")
            x = Integer.parseInt(field!!.get(obj).toString())
            sbar = context.resources.getDimensionPixelSize(x)
        } catch (e1: Exception) {
            e1.printStackTrace()
            return 0
        }
        return sbar
    }


    /**
     * 设置屏幕为横屏
     *
     * 还有一种就是在Activity中加属性android:screenOrientation="landscape"
     * 不设置Activity的android:configChanges时，切屏会重新调用各个生命周期，切横屏时会执行一次，切竖屏时会执行两次
     * 设置Activity的android:configChanges="orientation"时，切屏还是会重新调用各个生命周期，切横、竖屏时只会执行一次
     * 设置Activity的android:configChanges="orientation|keyboardHidden|screenSize"（4.0以上必须带最后一个参数）时
     * 切屏不会重新调用各个生命周期，只会执行onConfigurationChanged方法
     * @param activity activity
     */
    fun setLandscape(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置屏幕为竖屏

     * @param activity activity
     */
    fun setPortrait(activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 获取屏幕旋转角度
     *
     * @param activity activity
     * @return 屏幕旋转角度
     */
    fun getScreenRotation(activity: Activity): Int {
        when (activity.windowManager.defaultDisplay.rotation) {
            Surface.ROTATION_90 -> return 90
            Surface.ROTATION_180 -> return 180
            Surface.ROTATION_270 -> return 270
            else -> return 0
        }
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    fun captureWithStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels)
        view.destroyDrawingCache()
        return ret
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    fun captureWithoutStatusBar(activity: Activity): Bitmap {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val statusBarHeight = getStatusBarHeight(activity)
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight)
        view.destroyDrawingCache()
        return ret
    }

}
