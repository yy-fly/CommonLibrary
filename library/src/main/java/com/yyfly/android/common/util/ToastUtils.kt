package com.yyfly.android.common.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.yyfly.android.common.App
import com.yyfly.android.common.R


/**
 * Toast工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
@SuppressLint("InflateParams")
object ToastUtils {
    @ColorInt private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")
    @ColorInt private val ERROR_COLOR = Color.parseColor("#D50000")
    @ColorInt private val INFO_COLOR = Color.parseColor("#3F51B5")
    @ColorInt private val SUCCESS_COLOR = Color.parseColor("#388E3C")
    @ColorInt private val WARNING_COLOR = Color.parseColor("#FFA900")

    private val TOAST_TYPEFACE = "sans-serif-condensed"

    fun normal(message: String) {
        normal(App.context, message, Toast.LENGTH_SHORT, null, false)
    }

    fun normal(context: Context, message: String) {
        normal(context, message, Toast.LENGTH_SHORT, null, false)
    }

    fun normal(context: Context, message: String, icon: Drawable) {
        normal(context, message, Toast.LENGTH_SHORT, icon, true)
    }

    fun normal(context: Context, message: String, duration: Int) {
        normal(context, message, duration, null, false)
    }

    @JvmOverloads fun normal(context: Context, message: String, duration: Int, icon: Drawable?, withIcon: Boolean = true) {
        custom(context, message, icon, DEFAULT_TEXT_COLOR, duration, withIcon)
    }

    fun warning(message: String) {
        warning(App.context, message, Toast.LENGTH_SHORT, true)
    }

    @JvmOverloads fun warning(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = true) {
        custom(context, message, getDrawable(context, R.drawable.ic_info_outline_white_48dp),
                DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, withIcon, true)
    }

    fun info(message: String) {
        info(App.context, message, Toast.LENGTH_SHORT, true)
    }

    @JvmOverloads fun info(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = true) {
        custom(context, message, getDrawable(context, R.drawable.ic_info_outline_white_48dp), DEFAULT_TEXT_COLOR, INFO_COLOR, duration, withIcon, true)
    }

    fun success(message: String) {
        success(App.context, message, Toast.LENGTH_SHORT, true)
    }

    @JvmOverloads fun success(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = true){
        custom(context, message, getDrawable(context, R.drawable.ic_check_white_48dp), DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, withIcon, true)
    }

    fun error(message: String) {
        error(App.context, message, Toast.LENGTH_SHORT, true)
    }

    @JvmOverloads fun error(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = true) {
        custom(context, message, getDrawable(context, R.drawable.ic_clear_white_48dp),
                DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, withIcon, true)
    }

    fun custom(context: Context, message: String, icon: Drawable?, @ColorInt textColor: Int, duration: Int, withIcon: Boolean) {
        custom(context, message, icon, textColor, -1, duration, withIcon, false)
    }

    fun custom(context: Context, message: String, @DrawableRes iconRes: Int, @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int,
               withIcon: Boolean, shouldTint: Boolean) {
        custom(context, message, getDrawable(context, iconRes), textColor,
                tintColor, duration, withIcon, shouldTint)
    }

    fun custom(context: Context, message: String, icon: Drawable?, @ColorInt textColor: Int, @ColorInt tintColor: Int, duration: Int, withIcon: Boolean, shouldTint: Boolean) {
        val currentToast = Toast(context)
        val toastLayout = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_toast, null)
        val toastIcon = toastLayout.findViewById(R.id.toast_icon) as ImageView
        val toastTextView = toastLayout.findViewById(R.id.toast_text) as TextView
        val drawableFrame: Drawable

        if (shouldTint)
            drawableFrame = tint9PatchDrawableFrame(context, tintColor)
        else
            drawableFrame = getDrawable(context, R.drawable.bg_toast)
        setBackground(toastLayout, drawableFrame)

        if (withIcon) {
            if (icon == null)
                throw IllegalArgumentException("Avoid passing 'icon' as null if 'withIcon' is set to true")
            setBackground(toastIcon, icon)
        } else
            toastIcon.visibility = View.GONE

        toastTextView.setTextColor(textColor)
        toastTextView.text = message
        toastTextView.typeface = Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL)

        currentToast.view = toastLayout
        currentToast.duration = duration
        currentToast.show()
    }

    internal fun tint9PatchDrawableFrame(context: Context, @ColorInt tintColor: Int): Drawable {
        val toastDrawable = getDrawable(context, R.drawable.bg_toast) as NinePatchDrawable
        toastDrawable.colorFilter = PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return toastDrawable
    }

    internal fun setBackground(view: View, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.background = drawable
        else
            view.setBackgroundDrawable(drawable)
    }

    internal fun getDrawable(context: Context, @DrawableRes id: Int): Drawable {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return context.getDrawable(id)
        else
            return context.resources.getDrawable(id)
    }
}
