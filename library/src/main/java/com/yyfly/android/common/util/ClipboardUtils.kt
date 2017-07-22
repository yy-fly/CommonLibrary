package com.yyfly.android.common.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yyfly.android.common.App


/**
 * 剪贴板相关工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object ClipboardUtils {

    /**
     * 复制文本到剪贴板
     *
     * @param text 文本
     */
    fun copyText(text: CharSequence) {
        val clipboard = App.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText("text", text)
    }

    /**
     * 复制uri到剪贴板
     *
     * @param uri uri
     */
    fun copyUri(uri: Uri) {
        val clipboard = App.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newUri(App.context.getContentResolver(), "uri", uri)
    }

    /**
     * 获取剪贴板的意图
     *
     * @return 剪贴板的意图
     */
    val intent: Intent?
        get() {
            val clipboard = App.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                return clip.getItemAt(0).intent
            }
            return null
        }
    /**
     * 获取剪贴板的文本
     *
     * @return 剪贴板的文本
     */
    val text: CharSequence?
        get() {
            val clipboard = App.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                return clip.getItemAt(0).coerceToText(App.context)
            }
            return null
        }

    /**
     * 获取剪贴板的uri
     *
     * @return 剪贴板的uri
     */
    val uri: Uri?
        get() {
            val clipboard = App.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip
            if (clip != null && clip.itemCount > 0) {
                return clip.getItemAt(0).uri
            }
            return null
        }

    /**
     * 复制意图到剪贴板
     *
     * @param intent 意图
     */
    fun copyIntent(intent: Intent) {
        val clipboard = App.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newIntent("intent", intent)
    }

}
