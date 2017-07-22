package com.yyfly.android.common.util

/**
 * 常量相关工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object ConstUtils {

    /******************** SP相关常量  */
    /**
     * 设备id
     */
    val SP_IDENTIFIER = "SP_IDENTIFIER"

    /**
     * handler msg定义
     */
    /**
     * MSG_INFO
     */
    val MSG_INFO = 100
    /**
     * MSG_ERROR
     */
    val MSG_ERROR = 101


    /******************** 存储相关常量  */
    /**
     * KB与Byte的倍数
     */
    val KB = 1024
    /**
     * MB与Byte的倍数
     */
    val MB = 1048576
    /**
     * GB与Byte的倍数
     */
    val GB = 1073741824

    /******************** 时间相关常量  */
    /**
     * 秒与毫秒的倍数
     */
    val SEC = 1000
    /**
     * 分与毫秒的倍数
     */
    val MIN = 60000
    /**
     * 时与毫秒的倍数
     */
    val HOUR = 3600000
    /**
     * 天与毫秒的倍数
     */
    val DAY = 86400000

    enum class TimeUnit {
        MSEC,
        SEC,
        MIN,
        HOUR,
        DAY
    }

}