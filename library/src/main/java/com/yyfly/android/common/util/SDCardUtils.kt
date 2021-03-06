package com.yyfly.android.common.util

import android.annotation.TargetApi
import android.os.Build
import android.os.Environment
import android.os.StatFs

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

/**
 * SDCard工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object SDCardUtils {

    /**
     * 剩余内存限制
     */
    private val LOW_STORAGE_THRESHOLD = (1024 * 1024 * 10).toLong()

    /**
     * 判断SD卡是否可用
     *
     * @return true : 可用<br></br>false : 不可用
     */
    val isSDCardEnable: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()


    /**
     * 获得应用默认存储路径
     *
     * @return
     */
    val defaultAppPath: String
        get() = absoluteSDCardPath!! + AppUtils.applicationName!!

    /**
     * 获得sdcard路径
     *
     * @return
     */
    val absoluteSDCardPath: String?
        get() {
            if (isSDCardEnable) {
                return Environment.getExternalStorageDirectory().absolutePath + File.separator
            }
            return null
        }

    /**
     * 获取SD卡路径
     *
     * 先用shell，shell失败再普通方法获取，一般是/storage/emulated/0/
     * @return SD卡路径
     */
    val sdCardPath: String?
        get() {
            if (!isSDCardEnable) return null
            val cmd = "cat /proc/mounts"
            val run = Runtime.getRuntime()
            var bufferedReader: BufferedReader? = null
            try {
                val p = run.exec(cmd)
                bufferedReader = BufferedReader(InputStreamReader(BufferedInputStream(p.inputStream)))
                var lineStr: String? = bufferedReader.readLine()
                while ((lineStr) != null) {
                    if (lineStr.contains("sdcard") && lineStr.contains(".android_secure")) {
                        val strArray = lineStr.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (strArray.size >= 5) {
                            return strArray[1].replace("/.android_secure", "") + File.separator
                        }
                    }
                    if (p.waitFor() != 0 && p.exitValue() == 1) {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                IOCloseUtils.closeIO(bufferedReader)
            }
            return Environment.getExternalStorageDirectory().path + File.separator
        }

    /**
     * 获取可用的存储
     *
     * @return
     */
    val availableStorage: Long
        get() {
            var storageDirectory: String? = null
            storageDirectory = Environment.getExternalStorageDirectory().toString()

            try {
                val stat = StatFs(storageDirectory)
                val avaliableSize = stat.availableBlocks.toLong() * stat.blockSize.toLong()
                return avaliableSize
            } catch (ex: RuntimeException) {
                return 0
            }

        }

    /**
     * 检查内存是否充足
     *
     * @return
     */
    fun checkAvailableStorage(): Boolean {
        if (availableStorage < LOW_STORAGE_THRESHOLD) {
            return false
        }
        return true
    }

    /**
     * 获取SD卡信息
     *
     * @return SDCardInfo
     */
    val sdCardInfo: String?
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        get() {
            if (!isSDCardEnable) return null
            val sd = SDCardInfo()
            sd.isExist = true
            val sf = StatFs(Environment.getExternalStorageDirectory().path)
            sd.totalBlocks = sf.blockCountLong
            sd.blockByteSize = sf.blockSizeLong
            sd.availableBlocks = sf.availableBlocksLong
            sd.availableBytes = sf.availableBytes
            sd.freeBlocks = sf.freeBlocksLong
            sd.freeBytes = sf.freeBytes
            sd.totalBytes = sf.totalBytes
            return sd.toString()
        }

    class SDCardInfo {
        internal var isExist: Boolean = false
        internal var totalBlocks: Long = 0
        internal var freeBlocks: Long = 0
        internal var availableBlocks: Long = 0
        internal var blockByteSize: Long = 0
        internal var totalBytes: Long = 0
        internal var freeBytes: Long = 0
        internal var availableBytes: Long = 0

        override fun toString(): String {
            return "isExist=" + isExist +
                    "\ntotalBlocks=" + totalBlocks +
                    "\nfreeBlocks=" + freeBlocks +
                    "\navailableBlocks=" + availableBlocks +
                    "\nblockByteSize=" + blockByteSize +
                    "\ntotalBytes=" + totalBytes +
                    "\nfreeBytes=" + freeBytes +
                    "\navailableBytes=" + availableBytes
        }
    }

}
