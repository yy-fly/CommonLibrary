package com.yyfly.android.common.util

/**
 * Array Utils
 *
 *  * [.isEmpty] is null or its length is 0
 *  * [.getLast] get last element of the target element, before the first one
 * that match the target element front to back
 *  * [.getNext] get next element of the target element, after the first one
 * that match the target element front to back
 *  * [.getLast]
 *  * [.getLast]
 *  * [.getLast]
 *  * [.getNext]
 *  * [.getNext]
 *  * [.getNext]
 */
object ArrayUtils {


    /**
     * is null or its length is 0
     *
     * @param <V>
     * @param sourceArray
     * @return
    </V> */
    fun <V> isEmpty(sourceArray: Array<V>?): Boolean {
        return sourceArray == null || sourceArray.size == 0
    }

    /**
     * get last element of the target element, before the first one that match the target element front to back
     *
     *  * if array is empty, return defaultValue
     *  * if target element is not exist in array, return defaultValue
     *  * if target element exist in array and its index is not 0, return the last element
     *  * if target element exist in array and its index is 0, return the last one in array if isCircle is true, else
     * return defaultValue
     *
     * @param <V>
     * @param sourceArray
     * @param value value of target element
     * @param defaultValue default return value
     * @param isCircle whether is circle
     * @return
    </V> */
    fun <V> getLast(sourceArray: Array<V>, value: V, defaultValue: V?, isCircle: Boolean): V {
        if (isEmpty(sourceArray)) {
            return defaultValue!!
        }

        var currentPosition = -1
        for (i in sourceArray.indices) {
            if (ObjectUtils.isEquals(value, sourceArray[i])) {
                currentPosition = i
                break
            }
        }
        if (currentPosition == -1) {
            return defaultValue!!
        }

        if (currentPosition == 0) {
            return if (isCircle) sourceArray[sourceArray.size - 1] else defaultValue!!
        }
        return sourceArray[currentPosition - 1]
    }

    /**
     * get next element of the target element, after the first one that match the target element front to back
     *
     *  * if array is empty, return defaultValue
     *  * if target element is not exist in array, return defaultValue
     *  * if target element exist in array and not the last one in array, return the next element
     *  * if target element exist in array and the last one in array, return the first one in array if isCircle is
     * true, else return defaultValue
     *

     * @param <V>
     * @param sourceArray
     * @param value value of target element
     * @param defaultValue default return value
     * @param isCircle whether is circle
     * @return
    </V> */
    fun <V> getNext(sourceArray: Array<V>, value: V, defaultValue: V?, isCircle: Boolean): V {
        if (isEmpty(sourceArray)) {
            return defaultValue!!
        }

        var currentPosition = -1
        for (i in sourceArray.indices) {
            if (ObjectUtils.isEquals(value, sourceArray[i])) {
                currentPosition = i
                break
            }
        }
        if (currentPosition == -1) {
            return defaultValue!!
        }

        if (currentPosition == sourceArray.size - 1) {
            return if (isCircle) sourceArray[0] else defaultValue!!
        }
        return sourceArray[currentPosition + 1]
    }

    /**
     * @see {@link ArrayUtils.getLast
     */
    fun <V> getLast(sourceArray: Array<V>, value: V, isCircle: Boolean): V {
        return getLast(sourceArray, value, null, isCircle)
    }

    /**
     * @see {@link ArrayUtils.getNext
     */
    fun <V> getNext(sourceArray: Array<V>, value: V, isCircle: Boolean): V {
        return getNext(sourceArray, value, null, isCircle)
    }

    /**
     * @see {@link ArrayUtils.getLast
     */
    fun getLast(sourceArray: LongArray, value: Long, defaultValue: Long, isCircle: Boolean): Long {
        if (sourceArray.isEmpty()) {
            throw IllegalArgumentException("The length of source array must be greater than 0.")
        }

        val array = ObjectUtils.transformLongArray(sourceArray)
        return getLast(array, value, defaultValue, isCircle)

    }

    /**
     * @see {@link ArrayUtils.getNext
     */
    fun getNext(sourceArray: LongArray, value: Long, defaultValue: Long, isCircle: Boolean): Long {
        if (sourceArray.isEmpty()) {
            throw IllegalArgumentException("The length of source array must be greater than 0.")
        }

        val array = ObjectUtils.transformLongArray(sourceArray)
        return getNext(array, value, defaultValue, isCircle)
    }

    /**
     * @see {@link ArrayUtils.getLast
     */
    fun getLast(sourceArray: IntArray, value: Int, defaultValue: Int, isCircle: Boolean): Int {
        if (sourceArray.isEmpty()) {
            throw IllegalArgumentException("The length of source array must be greater than 0.")
        }

        val array = ObjectUtils.transformIntArray(sourceArray)
        return getLast(array, value, defaultValue, isCircle)

    }

    /**
     * @see {@link ArrayUtils.getNext
     */
    fun getNext(sourceArray: IntArray, value: Int, defaultValue: Int, isCircle: Boolean): Int {
        if (sourceArray.isEmpty()) {
            throw IllegalArgumentException("The length of source array must be greater than 0.")
        }

        val array = ObjectUtils.transformIntArray(sourceArray)
        return getNext(array, value, defaultValue, isCircle)
    }
}
