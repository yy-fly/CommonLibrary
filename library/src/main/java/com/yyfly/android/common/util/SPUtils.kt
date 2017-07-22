package com.yyfly.android.common.util

import android.content.Context
import android.content.SharedPreferences
import com.yyfly.android.common.App


/**
 * SP相关工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object SPUtils {

    /**
     * 获得SharedPreferences实例
     * @return
     */
    val sharePreferences: SharedPreferences?
        get() = App.context.getSharedPreferences(AppUtils.applicationName, Context.MODE_PRIVATE)

    /**
     * SP中写入String类型value
     *
     * @param key   键
     * @param value 值
     */
    fun setString(key: String, value: String) {
        val preferences = sharePreferences
        if (preferences != null) {
            val editor = preferences.edit()
            editor.remove(key)
            editor.putString(key, value)
            editor.commit()
        }
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads fun getString(key: String, defaultValue: String = ""): String {
        val preferences = sharePreferences
        if (preferences != null)
            return preferences.getString(key, defaultValue)
        else
            return defaultValue
    }

    /**
     * SP中写入int类型value
     *
     * @param key   键
     * @param value 值
     */
    fun setInt(key: String, value: Int) {
        val preferences = sharePreferences
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putInt(key, value)
            editor.commit()
        }
    }

    /**
     * SP中读取int
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads fun getInt(key: String, defaultValue: Int = -1): Int {
        val preferences = sharePreferences
        if (preferences != null)
            return preferences.getInt(key, defaultValue)
        else
            return defaultValue
    }

    /**
     * SP中写入long类型value
     *
     * @param key   键
     * @param value 值
     */
    fun setLong(key: String, value: Long) {
        val preferences = sharePreferences
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putLong(key, value)
            editor.commit()
        }
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads fun getLong(key: String, defaultValue: Long = -1L): Long {
        val preferences = sharePreferences
        if (preferences != null)
            return preferences.getLong(key, defaultValue)
        else
            return defaultValue
    }

    /**
     * SP中写入float类型value
     *
     * @param key   键
     * @param value 值
     */
    fun setFloat(key: String, value: Float) {
        val preferences = sharePreferences
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putFloat(key, value)
            editor.commit()
        }
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads fun getFloat(key: String, defaultValue: Float = -1f): Float {
        val preferences = sharePreferences
        if (preferences != null)
            return preferences.getFloat(key, defaultValue)
        else
            return defaultValue
    }

    /**
     * SP中写入boolean类型value
     *
     * @param key   键
     * @param value 值
     */
    fun setBoolean(key: String, value: Boolean) {
        val preferences = sharePreferences
        if (preferences != null) {
            val editor = preferences.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值`defaultValue`
     */
    @JvmOverloads fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val preferences = sharePreferences
        if (preferences != null)
            return preferences.getBoolean(key, defaultValue)
        else
            return defaultValue
    }

    /**
     * SP中获取所有键值对
     *
     * @return Map对象
     */
    val all: Map<String, *>?
        get() {
            val preferences = sharePreferences
            if (preferences != null) {
                return preferences.all
            } else {
                return null
            }
        }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    fun remove(key: String) {
        val preferences = sharePreferences
        if (preferences != null) {
            val editor = preferences.edit()
            editor.remove(key)
            editor.commit()
        }
    }

    /**
     * SP中是否存在该key
     *
     * @param key 键
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    operator fun contains(key: String): Boolean {
        val preferences = sharePreferences
        return preferences!!.contains(key)
    }

    /**
     * SP中清除所有数据
     */
    fun clearAll() {
        val preferences = sharePreferences
        preferences!!.edit().clear().commit()
    }
}
