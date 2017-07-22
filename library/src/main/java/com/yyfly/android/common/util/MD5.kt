package com.yyfly.android.common.util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * md5工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object MD5 {

    /**
     * 全局数组
     */
    private val strDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte the b byte
     * @return the string
     */
    private fun byteToArrayString(bByte: Byte): String {
        var iRet = bByte.toInt()
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256
        }
        val iD1 = iRet / 16
        val iD2 = iRet % 16
        return strDigits[iD1] + strDigits[iD2]
    }

    /**
     * 返回形式只为数字
     *
     * @param bByte the b byte
     * @return the string

     */
    private fun byteToNum(bByte: Byte): String {
        var iRet = bByte.toInt()
        println("iRet1=" + iRet)
        if (iRet < 0) {
            iRet += 256
        }
        return iRet.toString()
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte the b byte
     * @return the string
     */
    private fun byteToString(bByte: ByteArray): String {
        val sBuffer = StringBuffer()
        for (i in bByte.indices) {
            sBuffer.append(byteToArrayString(bByte[i]))
        }
        return sBuffer.toString()
    }

    /**
     * Get md 5 code string.
     *
     * @param strObj the str obj
     * @return the string
     */
    fun GetMD5Code(strObj: String): String? {
        var resultString: String? = null
        try {
            resultString = strObj
            val md = MessageDigest.getInstance("MD5")
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.toByteArray()))
        } catch (ex: NoSuchAlgorithmException) {
            ex.printStackTrace()
        }

        return resultString
    }

}
