package com.yyfly.android.common.util

import android.text.TextUtils
import java.util.*

/**
 * 列表工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object ListUtils {


    /** default join separator  */
    val DEFAULT_JOIN_SEPARATOR = ","

    /**
     * get size of list
     * getSize(null)   =   0;
     * getSize({})     =   0;
     * getSize({1})    =   1;
     * @param <V>
     * @param sourceList
     * @return if list is null or empty, return 0, else return [List.size].
     */
    fun <V> getSize(sourceList: List<V>?): Int {
        return sourceList?.size ?: 0
    }

    /**
     * is null or its size is 0
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
     */
    fun <V> isEmpty(sourceList: List<V>?): Boolean {
        return sourceList == null || sourceList.size == 0
    }

    /**
     * join list to string
     * join(null, '#')     =   "";
     * join({}, '#')       =   "";
     * join({a,b,c}, ' ')  =   "abc";
     * join({a,b,c}, '#')  =   "a#b#c";
     *
     * @param list
     * @param separator
     * @return join list to string. if list is empty, return ""
     */
    fun join(list: List<String>, separator: Char): String {
        return join(list, String(charArrayOf(separator)))
    }

    /**
     * join list to string. if separator is null, use [.DEFAULT_JOIN_SEPARATOR]
     *
     * join(null, "#")     =   "";
     * join({}, "#$")      =   "";
     * join({a,b,c}, null) =   "a,b,c";
     * join({a,b,c}, "")   =   "abc";
     * join({a,b,c}, "#")  =   "a#b#c";
     * join({a,b,c}, "#$") =   "a#$b#$c";
     *
     * @param list
     * @param separator
     * @return join list to string with separator. if list is empty, return ""
     */
    @JvmOverloads fun join(list: List<String>?, separator: String = DEFAULT_JOIN_SEPARATOR): String {
        return if (list == null) "" else TextUtils.join(separator, list)
    }

    /**
     * add distinct entry to list
     *
     * @param <V>
     * @param sourceList
     * @param entry
     * @return if entry already exist in sourceList, return false, else add it and return true.
    </V> */
    fun <V> addDistinctEntry(sourceList: MutableList<V>?, entry: V): Boolean {
        return if (sourceList != null && !sourceList.contains(entry)) sourceList.add(entry) else false
    }

    /**
     * add all distinct entry to list1 from list2
     *
     * @param <V>
     * @param sourceList
     * @param entryList
     * @return the count of entries be added
    </V> */
    fun <V> addDistinctList(sourceList: MutableList<V>?, entryList: List<V>): Int {
        if (sourceList == null || isEmpty(entryList)) {
            return 0
        }

        val sourceCount = sourceList.size
        for (entry in entryList) {
            if (!sourceList.contains(entry)) {
                sourceList.add(entry)
            }
        }
        return sourceList.size - sourceCount
    }

    /**
     * remove duplicate entries in list
     *
     * @param <V>
     * @param sourceList
     * @return the count of entries be removed
    </V> */
    fun <V> distinctList(sourceList: MutableList<V>): Int {
        if (isEmpty(sourceList)) {
            return 0
        }

        val sourceCount = sourceList.size
        var sourceListSize = sourceList.size
        for (i in 0..sourceListSize - 1) {
            var j = i + 1
            while (j < sourceListSize) {
                if (sourceList[i] == sourceList[j]) {
                    sourceList.removeAt(j)
                    sourceListSize = sourceList.size
                    j--
                }
                j++
            }
        }
        return sourceCount - sourceList.size
    }

    /**
     * add not null entry to list
     *
     * @param sourceList
     * @param value
     * @return
     * if sourceList is null, return false
     * if value is null, return false
     * return [List.add]
     */
    fun <V> addListNotNullValue(sourceList: MutableList<V>?, value: V?): Boolean {
        return if (sourceList != null && value != null) sourceList.add(value) else false
    }

    /**
     * @see {@link ArrayUtils.getLast
     */
    inline fun <reified V> getLast(sourceList: List<V>?, value: V): V? {
        return if (sourceList == null) null else ArrayUtils.getLast(sourceList.toTypedArray(), value, true)
    }

    /**
     * @see {@link ArrayUtils.getNext
     */
    inline fun <reified V> getNext(sourceList: List<V>?, value: V): V? {
        return if (sourceList == null) null else ArrayUtils.getNext(sourceList.toTypedArray(), value, true)
    }

    /**
     * invert list
     *
     * @param <V>
     * @param sourceList
     * @return
     */
    fun <V> invertList(sourceList: List<V>): List<V> {
        if (isEmpty(sourceList)) {
            return sourceList
        }

        val invertList = ArrayList<V>(sourceList.size)
        for (i in sourceList.indices.reversed()) {
            invertList.add(sourceList[i])
        }
        return invertList
    }
}
