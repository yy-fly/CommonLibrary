package com.yyfly.android.common.ui.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow

/**
 * 自定义PopWindow类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
class CustomPopWindow private constructor(private val mContext: Context) : PopupWindow.OnDismissListener {
    var width: Int = 0
    var height: Int = 0
    var mIsFocusable = true
    var mIsOutside = true
    var mResLayoutId = -1
    var mContentView: View? = null
    var mPopupWindow: PopupWindow? = null
    var mAnimationStyle = -1

    var mClippingEnabled = true//default is true
    var mIgnoreCheekPress = false
    var mInputMode = -1
    var mOnDismissListener: PopupWindow.OnDismissListener? = null
    var mSoftInputMode = -1
    var mTouchable = true//default is true
    var mOnTouchListener: View.OnTouchListener? = null

    var mWindow: Window? = null//当前Activity 的窗口
    /**
     * 弹出PopWindow 背景是否变暗，默认不会变暗。
     */
    var mIsBackgroundDark = false

    var mBackgroundDarkValue = 0f// 背景变暗的值，0 - 1

    val DEFAULT_ALPHA = 0.7f
    /**
     * 视图下方显示
     *
     * @param anchor
     * @param xOff
     * @param yOff
     * @return
     */
    fun showAsDropDown(anchor: View, xOff: Int, yOff: Int): CustomPopWindow {
        if (mPopupWindow != null) {
            mPopupWindow!!.showAsDropDown(anchor, xOff, yOff)
        }
        return this
    }

    fun showAsDropDown(anchor: View): CustomPopWindow {
        if (mPopupWindow != null) {
            mPopupWindow!!.showAsDropDown(anchor)
        }
        return this
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun showAsDropDown(anchor: View, xOff: Int, yOff: Int, gravity: Int): CustomPopWindow {
        if (mPopupWindow != null) {
            mPopupWindow!!.showAsDropDown(anchor, xOff, yOff, gravity)
        }
        return this
    }

    /**
     * 相对于父控件的位置（通过设置Gravity.CENTER，下方Gravity.BOTTOM等 ），可以设置具体位置坐标
     *
     * @param parent 父控件
     * @param gravity
     * @param x the popup's x location offset
     * @param y the popup's y location offset
     * @return
     */
    fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int): CustomPopWindow {
        if (mPopupWindow != null) {
            mPopupWindow!!.showAtLocation(parent, gravity, x, y)
        }
        return this
    }

    /**
     * 添加一些属性设置
     * @param popupWindow
     */
    private fun apply(popupWindow: PopupWindow) {
        popupWindow.isClippingEnabled = mClippingEnabled
        if (mIgnoreCheekPress) {
            popupWindow.setIgnoreCheekPress()
        }
        if (mInputMode != -1) {
            popupWindow.inputMethodMode = mInputMode
        }
        if (mSoftInputMode != -1) {
            popupWindow.softInputMode = mSoftInputMode
        }
        if (mOnDismissListener != null) {
            popupWindow.setOnDismissListener(mOnDismissListener)
        }
        if (mOnTouchListener != null) {
            popupWindow.setTouchInterceptor(mOnTouchListener)
        }
        popupWindow.isTouchable = mTouchable


    }

    private fun build(): PopupWindow {
        if (mContentView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(mResLayoutId, null)
        }

        // 获取当前Activity的window
        val activity = mContentView!!.context as Activity
        if (activity != null && mIsBackgroundDark) {
            //如果设置的值在0 - 1的范围内，则用设置的值，否则用默认值
            val alpha = if (mBackgroundDarkValue > 0 && mBackgroundDarkValue < 1) mBackgroundDarkValue else DEFAULT_ALPHA
            mWindow = activity.window
            val params = mWindow!!.attributes
            params.alpha = alpha
            mWindow!!.attributes = params
        }

        if (width != 0 && height != 0) {
            mPopupWindow = PopupWindow(mContentView, width, height)
        } else {
            mPopupWindow = PopupWindow(mContentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        if (mAnimationStyle != -1) {
            mPopupWindow!!.animationStyle = mAnimationStyle
        }

        apply(mPopupWindow!!)//设置一些属性

        mPopupWindow!!.isFocusable = mIsFocusable
        mPopupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mPopupWindow!!.isOutsideTouchable = mIsOutside

        if (width == 0 || height == 0) {
            mPopupWindow!!.contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            //如果外面没有设置宽高的情况下，计算宽高并赋值
            width = mPopupWindow!!.contentView.measuredWidth
            height = mPopupWindow!!.contentView.measuredHeight
        }

        // 添加dissmiss 监听
        mPopupWindow!!.setOnDismissListener(this)
        mPopupWindow!!.update()

        return mPopupWindow!!
    }

    override fun onDismiss() {
        dismissPopupWindow()
    }

    /**
     * 关闭popWindow
     */
    fun dismissPopupWindow() {
        //如果设置了背景变暗，那么在dissmiss的时候需要还原
        if (mWindow != null) {
            val params = mWindow!!.attributes
            params.alpha = 1.0f
            mWindow!!.attributes = params
        }
        if (mPopupWindow != null && mPopupWindow!!.isShowing) {
            mPopupWindow!!.dismiss()
        }
    }


    class PopupWindowBuilder(context: Context) {
        private val mCustomPopWindow: CustomPopWindow

        init {
            mCustomPopWindow = CustomPopWindow(context)
        }

        fun size(width: Int, height: Int): PopupWindowBuilder {
            mCustomPopWindow.width = width
            mCustomPopWindow.height = height
            return this
        }

        fun setFocusable(focusable: Boolean): PopupWindowBuilder {
            mCustomPopWindow.mIsFocusable = focusable
            return this
        }

        fun setView(resLayoutId: Int): PopupWindowBuilder {
            mCustomPopWindow.mResLayoutId = resLayoutId
            mCustomPopWindow.mContentView = null
            return this
        }

        fun setView(view: View): PopupWindowBuilder {
            mCustomPopWindow.mContentView = view
            mCustomPopWindow.mResLayoutId = -1
            return this
        }

        fun setOutsideTouchable(outsideTouchable: Boolean): PopupWindowBuilder {
            mCustomPopWindow.mIsOutside = outsideTouchable
            return this
        }

        /**
         * 设置弹窗动画
         * @param animationStyle
         * @return
         */
        fun setAnimationStyle(animationStyle: Int): PopupWindowBuilder {
            mCustomPopWindow.mAnimationStyle = animationStyle
            return this
        }

        fun setClippingEnable(enable: Boolean): PopupWindowBuilder {
            mCustomPopWindow.mClippingEnabled = enable
            return this
        }

        fun setIgnoreCheekPress(ignoreCheekPress: Boolean): PopupWindowBuilder {
            mCustomPopWindow.mIgnoreCheekPress = ignoreCheekPress
            return this
        }

        fun setInputMethodMode(mode: Int): PopupWindowBuilder {
            mCustomPopWindow.mInputMode = mode
            return this
        }

        fun setOnDissmissListener(onDissmissListener: PopupWindow.OnDismissListener): PopupWindowBuilder {
            mCustomPopWindow.mOnDismissListener = onDissmissListener
            return this
        }

        fun setSoftInputMode(softInputMode: Int): PopupWindowBuilder {
            mCustomPopWindow.mSoftInputMode = softInputMode
            return this
        }

        fun setTouchable(touchable: Boolean): PopupWindowBuilder {
            mCustomPopWindow.mTouchable = touchable
            return this
        }

        fun setTouchIntercepter(touchIntercepter: View.OnTouchListener): PopupWindowBuilder {
            mCustomPopWindow.mOnTouchListener = touchIntercepter
            return this
        }

        /**
         * 设置背景变暗是否可用
         * @param isDark
         * @return
         */
        fun enableBackgroundDark(isDark: Boolean): PopupWindowBuilder {
            mCustomPopWindow.mIsBackgroundDark = isDark
            return this
        }

        /**
         * 设置背景变暗的值
         * @param darkValue
         * @return
         */
        fun setBgDarkAlpha(darkValue: Float): PopupWindowBuilder {
            mCustomPopWindow.mBackgroundDarkValue = darkValue
            return this
        }

        fun create(): CustomPopWindow {
            //构建PopWindow
            mCustomPopWindow.build()
            return mCustomPopWindow
        }
    }

}
