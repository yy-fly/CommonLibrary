package com.yyfly.android.common

import android.content.Context
import com.yyfly.android.common.util.DisplayUtils
import kotlin.properties.Delegates

/**
 * 应用实体

 * @author : yyfly / developer@yyfly.com
 * *
 * @version : 1.0
 */
object App {

    var context: Context by Delegates.notNull()

    /**
     * 日志开关
     */
    var logFlag = false
    /**
     * 日志级别，默认日志为VERBOSE
     */
    var DEFAULT_LEVEL = android.util.Log.VERBOSE

    /**
     * APP 初始化相关操作

     * @param cxt
     * *
     * @param debug      是否调试
     */
    @JvmOverloads fun init(cxt: Context, debug: Boolean = true) {
        context = cxt
        if (debug) {
            logFlag = true
            DEFAULT_LEVEL = android.util.Log.VERBOSE
        } else {
            logFlag = false
            DEFAULT_LEVEL = android.util.Log.WARN
        }
        DisplayUtils.init(context)
    }

}

