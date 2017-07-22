package com.yyfly.android.common.util

import com.yyfly.android.common.App


/**
 * 统一日志管理
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object Log {

    fun v(tag: String, msg: String) {
        log(android.util.Log.VERBOSE, tag, classNameAndLineNumber + msg, null)
    }

    fun v(tag: String, msg: String, tr: Throwable) {
        log(android.util.Log.VERBOSE, tag, classNameAndLineNumber + msg, tr)
    }

    fun d(tag: String, msg: String) {
        log(android.util.Log.DEBUG, tag, classNameAndLineNumber + msg, null)
    }

    fun d(tag: String, msg: String, tr: Throwable) {
        log(android.util.Log.DEBUG, tag, classNameAndLineNumber + msg, tr)
    }

    fun i(tag: String, msg: String) {
        log(android.util.Log.INFO, tag, classNameAndLineNumber + msg, null)
    }

    fun i(tag: String, msg: String, tr: Throwable) {
        log(android.util.Log.INFO, tag, classNameAndLineNumber + msg, tr)
    }

    fun w(tag: String, msg: String) {
        log(android.util.Log.WARN, tag, classNameAndLineNumber + msg, null)
    }

    fun w(tag: String, msg: String, tr: Throwable) {
        log(android.util.Log.WARN, tag, classNameAndLineNumber + msg, tr)
    }

    fun e(tag: String, msg: String) {
        log(android.util.Log.ERROR, tag, classNameAndLineNumber + msg, null)
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        log(android.util.Log.ERROR, tag, classNameAndLineNumber + msg, tr)
    }

    /**
     * 日志打印
     *
     * @param level
     * @param tag
     * @param msg
     * @param e
     */
    fun log(level: Int, tag: String, msg: String, e: Throwable?) {
        if (App.logFlag) {
            when (level) {
                android.util.Log.VERBOSE -> if (level >= App.DEFAULT_LEVEL) {
                    android.util.Log.v(tag, msg, e)
                }
                android.util.Log.DEBUG -> if (level >= App.DEFAULT_LEVEL) {
                    android.util.Log.d(tag, msg, e)
                }
                android.util.Log.INFO -> if (level >= App.DEFAULT_LEVEL) {
                    android.util.Log.i(tag, msg, e)
                }
                android.util.Log.WARN -> if (level >= App.DEFAULT_LEVEL) {
                    android.util.Log.w(tag, msg, e)
                }
                android.util.Log.ERROR -> if (level >= App.DEFAULT_LEVEL) {
                    android.util.Log.e(tag, msg, e)
                }
                else -> {
                }
            }
        }


    }

    /**
     * @desc 打印调用函数名和行号
     *
     * @param
     */
    val classNameAndLineNumber: String
        get() {
            val stacks = Thread.currentThread().stackTrace
            var msg = ""
            if (stacks[4] != null) {
                val names = stacks[4].className.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val className = names[names.size - 1]
                msg = className + ":" + stacks[4].lineNumber + " 	 "
            }
            return msg
        }

}
