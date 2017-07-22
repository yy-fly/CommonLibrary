package com.yyfly.android.common.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.text.Html
import android.view.View

/**
 * 对话框工具类

 * @author : yyfly / developer@yyfly.com
 * *
 * @version : 1.0
 */
object DialogUtils {

    fun dialogBuilder(context: Context, title: String?, msg: String?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        if (msg != null) {
            builder.setMessage(msg)
        }
        if (title != null) {
            builder.setTitle(title)
        }
        return builder
    }

    fun dialogBuilder(context: Context, title: String?, msg: String?, i: Int): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        if (msg != null) {
            builder.setMessage(Html.fromHtml(msg))
        }
        if (title != null) {
            builder.setTitle(title)
        }
        return builder
    }


    fun dialogBuilder(context: Context, title: Int, view: View?): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        if (view != null) {
            builder.setView(view)
        }
        if (title > 0) {
            builder.setTitle(title)
        }
        return builder
    }

    fun dialogBuilder(context: Context, titleResId: Int, msgResId: Int): AlertDialog.Builder {
        val title = if (titleResId > 0) context.resources.getString(titleResId) else null
        val msg = if (msgResId > 0) context.resources.getString(msgResId) else null
        return dialogBuilder(context, title, msg)
    }

    fun showTips(context: Context, title: Int, des: Int): Dialog {
        return showTips(context, context.getString(title), context.getString(des))
    }

    fun showTips(context: Context, title: Int, des: Int, btn: Int, dismissListener: DialogInterface.OnDismissListener): Dialog {
        return showTips(context, context.getString(title), context.getString(des), context.getString(btn), dismissListener)
    }

    @JvmOverloads fun showTips(context: Context, title: String, des: String, btn: String? = null, dismissListener: DialogInterface.OnDismissListener? = null): Dialog {
        val builder = dialogBuilder(context, title, des)
        builder.setCancelable(true)
        builder.setPositiveButton(btn, null)
        val dialog = builder.show()
        dialog.setCanceledOnTouchOutside(true)
        dialog.setOnDismissListener(dismissListener)
        return dialog
    }

}
