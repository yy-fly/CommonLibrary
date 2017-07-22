package com.yyfly.android.common.util

import com.google.gson.GsonBuilder

/**
 * Gson工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object GsonUtils {

    /**
     * 解析json字符串
     * @param jsonStr
     * *
     * @param t
     * *
     * @param <T>
     * *
     * @return
    </T> */
    fun <T> parseJSON(jsonStr: String, t: Class<T>): T {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        val bean = gson.fromJson(jsonStr, t)
        return bean
    }

    /**
     * 转为json字符
     * @param object
     * *
     * @return
     */
    fun toJson(`object`: Any): String {
        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
        return gson.toJson(`object`)
    }

}
