package com.yyfly.android.common.util

import java.io.*
import java.net.UnknownHostException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * 文件相关工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object FileUtils {

    /**
     * 根据文件路径获取文件
     * 
     * @param filePath 文件路径
     * @return 文件
     */
    fun getFileByPath(filePath: String): File? {
        return if (StringUtils.isBlank(filePath)) null else File(filePath)
    }

    /**
     * 判断文件是否存在
     * 
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(filePath: String): Boolean {
        return isFileExists(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    /**
     * 重命名文件
     *
     * @param filePath 文件路径
     * @param newName  新名称
     * @return `true`: 重命名成功<br></br>`false`: 重命名失败
     */
    fun rename(filePath: String, newName: String): Boolean {
        return rename(getFileByPath(filePath), newName)
    }

    /**
     * 重命名文件
     *
     * @param file    文件
     * @param newName 新名称
     * @return `true`: 重命名成功<br></br>`false`: 重命名失败
     */
    fun rename(file: File?, newName: String): Boolean {
        // 文件为空返回false
        if (file == null) return false
        // 文件不存在返回false
        if (!file.exists()) return false
        // 新的文件名为空返回false
        if (StringUtils.isBlank(newName)) return false
        // 如果文件名没有改变返回true
        if (newName == file.name) return true
        val newFile = File(file.parent + File.separator + newName)
        // 如果重命名的文件已存在返回false
        return !newFile.exists() && file.renameTo(newFile)
    }

    /**
     * 判断是否是目录
     *
     * @param dirPath 目录路径
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isDir(dirPath: String): Boolean {
        getFileByPath(dirPath)?.let {
            return isDir(it)
        }
        return false
    }

    /**
     * 判断是否是目录
     *
     * @param file 文件
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isDir(file: File): Boolean {
        return isFileExists(file) && file.isDirectory
    }

    /**
     * 判断是否是文件
     *
     * @param filePath 文件路径
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isFile(filePath: String): Boolean {
        getFileByPath(filePath)?.let {
            return isFile(it)
        }
        return false
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isFile(file: File): Boolean {
        return isFileExists(file) && file.isFile
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param dirPath 目录路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsDir(dirPath: String): Boolean {
        return createOrExistsDir(getFileByPath(dirPath))
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsDir(file: File?): Boolean {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(filePath: String): Boolean {
        return createOrExistsFile(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return `true`: 存在或创建成功<br></br>`false`: 不存在或创建失败
     */
    fun createOrExistsFile(file: File?): Boolean {
        if (file == null) return false
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile
        if (!createOrExistsDir(file.parentFile)) return false
        try {
            return file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param filePath 文件路径
     * @return `true`: 创建成功<br></br>`false`: 创建失败
     */
    fun createFileByDeleteOldFile(filePath: String): Boolean {
        return createFileByDeleteOldFile(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在，存在则在创建之前删除
     *
     * @param file 文件
     * @return `true`: 创建成功<br></br>`false`: 创建失败
     */
    fun createFileByDeleteOldFile(file: File?): Boolean {
        if (file == null) return false
        // 文件存在并且删除失败返回false
        if (file.exists() && file.isFile && !file.delete()) return false
        // 创建目录失败返回false
        if (!createOrExistsDir(file.parentFile)) return false
        try {
            return file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 复制或移动目录
     *
     * @param srcDirPath  源目录路径
     * @param destDirPath 目标目录路径
     * @param isMove      是否移动
     * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
     */
    private fun copyOrMoveDir(srcDirPath: String, destDirPath: String, isMove: Boolean): Boolean {
        return copyOrMoveDir(getFileByPath(srcDirPath), getFileByPath(destDirPath), isMove)
    }

    /**
     * 复制或移动目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @param isMove  是否移动
     * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
     */
    private fun copyOrMoveDir(srcDir: File?, destDir: File?, isMove: Boolean): Boolean {
        if (srcDir == null || destDir == null) return false
        // 如果目标目录在源目录中则返回false，看不懂的话好好想想递归怎么结束
        // srcPath : F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res
        // destPath: F:\\MyGithub\\AndroidUtilCode\\utilcode\\src\\test\\res1
        // 为防止以上这种情况出现出现误判，须分别在后面加个路径分隔符
        val srcPath = srcDir.path + File.separator
        val destPath = destDir.path + File.separator
        if (destPath.contains(srcPath)) return false
        // 源文件不存在或者不是目录则返回false
        if (!srcDir.exists() || !srcDir.isDirectory) return false
        // 目标目录不存在返回false
        if (!createOrExistsDir(destDir)) return false
        val files = srcDir.listFiles()
        for (file in files) {
            val oneDestFile = File(destPath + file.name)
            if (file.isFile) {
                // 如果操作失败返回false
                if (!copyOrMoveFile(file, oneDestFile, isMove)) return false
            } else if (file.isDirectory) {
                // 如果操作失败返回false
                if (!copyOrMoveDir(file, oneDestFile, isMove)) return false
            }
        }
        return !isMove || deleteDir(srcDir)
    }

    /**
     * 复制或移动文件
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @param isMove       是否移动
     * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
     */
    private fun copyOrMoveFile(srcFilePath: String, destFilePath: String, isMove: Boolean): Boolean {
        return copyOrMoveFile(getFileByPath(srcFilePath), getFileByPath(destFilePath), isMove)
    }

    /**
     * 复制或移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @param isMove   是否移动
     * @return `true`: 复制或移动成功<br></br>`false`: 复制或移动失败
     */
    private fun copyOrMoveFile(srcFile: File?, destFile: File?, isMove: Boolean): Boolean {
        if (srcFile == null || destFile == null) return false
        // 源文件不存在或者不是文件则返回false
        if (!srcFile.exists() || !srcFile.isFile) return false
        // 目标文件存在且是文件则返回false
        if (destFile.exists() && destFile.isFile) return false
        // 目标目录不存在返回false
        if (!createOrExistsDir(destFile.parentFile)) return false
        try {
            return writeFileFromIS(destFile, FileInputStream(srcFile), false) && !(isMove && !deleteFile(srcFile))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 复制目录
     *
     * @param srcDirPath  源目录路径
     * @param destDirPath 目标目录路径
     * @return `true`: 复制成功<br></br>`false`: 复制失败
     */
    fun copyDir(srcDirPath: String, destDirPath: String): Boolean {
        var src = getFileByPath(srcDirPath)
        var dest = getFileByPath(destDirPath)
        if (null == src || null == dest) return false
        return copyDir(src, dest)
    }

    /**
     * 复制目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @return `true`: 复制成功<br></br>`false`: 复制失败
     */
    fun copyDir(srcDir: File, destDir: File): Boolean {
        return copyOrMoveDir(srcDir, destDir, false)
    }

    /**
     * 复制文件
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @return `true`: 复制成功<br></br>`false`: 复制失败
     */
    fun copyFile(srcFilePath: String, destFilePath: String): Boolean {
        var src = getFileByPath(srcFilePath)
        var dest = getFileByPath(destFilePath)
        if (null == src || null == dest) return false
        return copyFile(src, dest)
    }

    /**
     * 复制文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return `true`: 复制成功<br></br>`false`: 复制失败
     */
    fun copyFile(srcFile: File, destFile: File): Boolean {
        return copyOrMoveFile(srcFile, destFile, false)
    }

    /**
     * 移动目录
     *
     * @param srcDirPath  源目录路径
     * @param destDirPath 目标目录路径
     * @return `true`: 移动成功<br></br>`false`: 移动失败
     */
    fun moveDir(srcDirPath: String, destDirPath: String): Boolean {
        var src = getFileByPath(srcDirPath)
        var dest = getFileByPath(destDirPath)
        if (null == src || null == dest) return false
        return moveDir(src, dest)
    }

    /**
     * 移动目录
     *
     * @param srcDir  源目录
     * @param destDir 目标目录
     * @return `true`: 移动成功<br></br>`false`: 移动失败
     */
    fun moveDir(srcDir: File, destDir: File): Boolean {
        return copyOrMoveDir(srcDir, destDir, true)
    }

    /**
     * 移动文件
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @return `true`: 移动成功<br></br>`false`: 移动失败
     */
    fun moveFile(srcFilePath: String, destFilePath: String): Boolean {
        var src = getFileByPath(srcFilePath)
        var dest = getFileByPath(destFilePath)
        if (null == src || null == dest) return false
        return moveFile(src, dest)
    }

    /**
     * 移动文件
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return `true`: 移动成功<br></br>`false`: 移动失败
     */
    fun moveFile(srcFile: File, destFile: File): Boolean {
        return copyOrMoveFile(srcFile, destFile, true)
    }

    /**
     * 删除目录
     *
     * @param dirPath 目录路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteDir(dirPath: String): Boolean {
        return deleteDir(getFileByPath(dirPath))
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // 目录不存在返回true
        if (!dir.exists()) return true
        // 不是目录返回false
        if (!dir.isDirectory) return false
        // 现在文件存在且是文件夹
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!deleteFile(file)) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }

    /**
     * 删除文件
     *
     * @param srcFilePath 文件路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(srcFilePath: String): Boolean {
        return deleteFile(getFileByPath(srcFilePath))
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * 删除目录下的所有文件
     *
     * @param dirPath 目录路径
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFilesInDir(dirPath: String): Boolean {
        return deleteFilesInDir(getFileByPath(dirPath))
    }

    /**
     * 删除目录下的所有文件
     *
     * @param dir 目录
     * @return `true`: 删除成功<br></br>`false`: 删除失败
     */
    fun deleteFilesInDir(dir: File?): Boolean {
        if (dir == null) return false
        // 目录不存在返回true
        if (!dir.exists()) return true
        // 不是目录返回false
        if (!dir.isDirectory) return false
        // 现在文件存在且是文件夹
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!deleteFile(file)) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return true
    }

    /**
     * 获取目录下所有文件
     *
     * @param dirPath     目录路径
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDir(dirPath: String, isRecursive: Boolean): List<File>? {
        var src = getFileByPath(dirPath)
        if (null == src) return null
        return listFilesInDir(src, isRecursive)
    }

    /**
     * 获取目录下所有文件
     *
     * @param dir         目录
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDir(dir: File, isRecursive: Boolean): List<File>? {
        if (!isDir(dir)) return null
        if (isRecursive) return listFilesInDir(dir)
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            Collections.addAll(list, *files)
        }
        return list
    }

    /**
     * 获取目录下所有文件包括子目录
     *
     * @param dirPath 目录路径
     * @return 文件链表
     */
    fun listFilesInDir(dirPath: String): List<File>? {
        var src = getFileByPath(dirPath)
        if (src == null) return null
        return listFilesInDir(src)
    }

    /**
     * 获取目录下所有文件包括子目录
     *
     * @param dir 目录
     * @return 文件链表
     */
    fun listFilesInDir(dir: File): List<File>? {
        if (!isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                list.add(file)
                if (file.isDirectory) {
                    list.addAll(listFilesInDir(file)!!)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有后缀名为suffix的文件
     *
     * 大小写忽略
     *
     * @param dirPath     目录路径
     * @param suffix      后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, suffix: String, isRecursive: Boolean): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix, isRecursive)
    }

    /**
     * 获取目录下所有后缀名为suffix的文件
     *
     * 大小写忽略
     *
     * @param dir         目录
     * @param suffix      后缀名
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, suffix: String, isRecursive: Boolean): List<File>? {
        if (isRecursive) return listFilesInDirWithFilter(dir, suffix)
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.name.toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有后缀名为suffix的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dirPath 目录路径
     * @param suffix  后缀名
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, suffix: String): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), suffix)
    }

    /**
     * 获取目录下所有后缀名为suffix的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dir    目录
     * @param suffix 后缀名
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, suffix: String): List<File>? {
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.name.toUpperCase().endsWith(suffix.toUpperCase())) {
                    list.add(file)
                }
                if (file.isDirectory) {
                    list.addAll(listFilesInDirWithFilter(file, suffix)!!)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有符合filter的文件
     *
     * @param dirPath     目录路径
     * @param filter      过滤器
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, filter: FilenameFilter, isRecursive: Boolean): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive)
    }

    /**
     * 获取目录下所有符合filter的文件
     *
     * @param dir         目录
     * @param filter      过滤器
     * @param isRecursive 是否递归进子目录
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, filter: FilenameFilter, isRecursive: Boolean): List<File>? {
        if (isRecursive) return listFilesInDirWithFilter(dir, filter)
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (filter.accept(file.parentFile, file.name)) {
                    list.add(file)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下所有符合filter的文件包括子目录
     *
     * @param dirPath 目录路径
     * @param filter  过滤器
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dirPath: String, filter: FilenameFilter): List<File>? {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter)
    }

    /**
     * 获取目录下所有符合filter的文件包括子目录
     *
     * @param dir    目录
     * @param filter 过滤器
     * @return 文件链表
     */
    fun listFilesInDirWithFilter(dir: File?, filter: FilenameFilter): List<File>? {
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (filter.accept(file.parentFile, file.name)) {
                    list.add(file)
                }
                if (file.isDirectory) {
                    list.addAll(listFilesInDirWithFilter(file, filter)!!)
                }
            }
        }
        return list
    }

    /**
     * 获取目录下指定文件名的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dirPath  目录路径
     * @param fileName 文件名
     * @return 文件链表
     */
    fun searchFileInDir(dirPath: String, fileName: String): List<File>? {
        return searchFileInDir(getFileByPath(dirPath), fileName)
    }

    /**
     * 获取目录下指定文件名的文件包括子目录
     *
     * 大小写忽略
     *
     * @param dir      目录
     * @param fileName 文件名
     * @return 文件链表
     */
    fun searchFileInDir(dir: File?, fileName: String): List<File>? {
        if (dir == null || !isDir(dir)) return null
        val list = ArrayList<File>()
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.name.toUpperCase() == fileName.toUpperCase()) {
                    list.add(file)
                }
                if (file.isDirectory) {
                    list.addAll(searchFileInDir(file, fileName)!!)
                }
            }
        }
        return list
    }

    /**
     * 将输入流写入文件
     *
     * @param filePath 路径
     * @param is       输入流
     * @param append   是否追加在文件末
     * @return `true`: 写入成功<br></br>`false`: 写入失败
     */
    fun writeFileFromIS(filePath: String, `is`: InputStream, append: Boolean): Boolean {
        return writeFileFromIS(getFileByPath(filePath), `is`, append)
    }

    /**
     * 将输入流写入文件
     *
     * @param file   文件
     * @param is     输入流
     * @param append 是否追加在文件末
     * @return `true`: 写入成功<br></br>`false`: 写入失败
     */
    fun writeFileFromIS(file: File?, inputStream: InputStream?, append: Boolean): Boolean {
        if (file == null || inputStream == null) return false
        if (!createOrExistsFile(file)) return false
        var os: OutputStream? = null
        try {
            os = BufferedOutputStream(FileOutputStream(file, append))
            val data = ByteArray(1024)
            var len: Int = inputStream.read(data, 0, 1024)
            while ((len) != -1) {
                os.write(data, 0, len)
            }
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            IOCloseUtils.closeIO(inputStream, os!!)
        }
    }

    /**
     * 将字符串写入文件
     *
     * @param filePath 文件路径
     * @param content  写入内容
     * @param append   是否追加在文件末
     * @return `true`: 写入成功<br></br>`false`: 写入失败
     */
    fun writeFileFromString(filePath: String, content: String, append: Boolean): Boolean {
        return writeFileFromString(getFileByPath(filePath), content, append)
    }

    /**
     * 将字符串写入文件
     *
     * @param file    文件
     * @param content 写入内容
     * @param append  是否追加在文件末
     * @return `true`: 写入成功<br></br>`false`: 写入失败
     */
    fun writeFileFromString(file: File?, content: String?, append: Boolean): Boolean {
        if (file == null || content == null) return false
        if (!createOrExistsFile(file)) return false
        var bw: BufferedWriter? = null
        try {
            bw = BufferedWriter(FileWriter(file, append))
            bw.write(content)
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        } finally {
            IOCloseUtils.closeIO(bw!!)
        }
    }


    /**
     * 指定编码按行读取文件到字符串中
     *
     * @param filePath    文件路径
     * @param charsetName 编码格式
     * @return 字符串
     */
    fun readFile2String(filePath: String, charsetName: String): String? {
        return readFile2String(getFileByPath(filePath), charsetName)
    }

    /**
     * 指定编码按行读取文件到字符串中
     *
     * @param file        文件
     * @param charsetName 编码格式
     * @return 字符串
     */
    fun readFile2String(file: File?, charsetName: String): String? {
        if (file == null) return null
        var reader: BufferedReader? = null
        try {
            val sb = StringBuilder()
            if (StringUtils.isBlank(charsetName)) {
                reader = BufferedReader(InputStreamReader(FileInputStream(file)))
            } else {
                reader = BufferedReader(InputStreamReader(FileInputStream(file), charsetName))
            }
            var line: String ?= reader.readLine()
            while ((line) != null) {
                sb.append(line).append("\r\n")// windows系统换行为\r\n，Linux为\n
            }
            // 要去除最后的换行符
            return sb.delete(sb.length - 2, sb.length).toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            IOCloseUtils.closeIO(reader!!)
        }
    }


    /**
     * 获取文件最后修改的毫秒时间戳
     *
     * @param filePath 文件路径
     * @return 文件最后修改的毫秒时间戳
     */
    fun getFileLastModified(filePath: String): Long {
        return getFileLastModified(getFileByPath(filePath))
    }

    /**
     * 获取文件最后修改的毫秒时间戳
     *
     * @param file 文件
     * @return 文件最后修改的毫秒时间戳
     */
    fun getFileLastModified(file: File?): Long {
        if (file == null) return -1
        return file.lastModified()
    }

    /**
     * 简单获取文件编码格式
     *
     * @param file 文件
     * @return 文件编码
     */
    fun getFileCharsetSimple(file: File): String {
        var p = 0
        var `is`: InputStream? = null
        try {
            `is` = BufferedInputStream(FileInputStream(file))
            p = (`is`.read() shl 8) + `is`.read()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            IOCloseUtils.closeIO(`is`!!)
        }
        when (p) {
            0xefbb -> return "UTF-8"
            0xfffe -> return "Unicode"
            0xfeff -> return "UTF-16BE"
            else -> return "GBK"
        }
    }

    /**
     * 获取目录长度
     *
     * @param dir 目录
     * @return 文件大小
     */
    fun getDirLength(dir: File): Long {
        if (!isDir(dir)) return -1
        var len: Long = 0
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isDirectory) {
                    len += getDirLength(file)
                } else {
                    len += file.length()
                }
            }
        }
        return len
    }

    /**
     * 获取文件长度
     *
     * @param file 文件
     * @return 文件大小
     */
    fun getFileLength(file: File): Long {
        if (!isFile(file)) return -1
        return file.length()
    }

    /**
     * 获取文件的MD5校验码
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */
    fun getFileMD5(file: File?): ByteArray? {
        if (file == null) return null
        var dis: DigestInputStream? = null
        try {
            val fis = FileInputStream(file)
            var md = MessageDigest.getInstance("MD5")
            dis = DigestInputStream(fis, md)
            val buffer = ByteArray(1024 * 256)
            while (dis.read(buffer) > 0)
                md = dis.messageDigest
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            IOCloseUtils.closeIO(dis!!)
        }
        return null
    }

    /**
     * 获取全路径中的最长目录
     *
     * @param file 文件
     * @return filePath最长目录
     */
    fun getDirName(file: File?): String? {
        if (file == null) return null
        return getDirName(file.path)
    }

    /**
     * 获取全路径中的最长目录
     *
     * @param filePath 文件路径
     * @return filePath最长目录
     */
    fun getDirName(filePath: String): String {
        if (StringUtils.isBlank(filePath)) return filePath
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastSep == -1) "" else filePath.substring(0, lastSep + 1)
    }

    /**
     * 获取全路径中的文件名
     *
     * @param file 文件
     * @return 文件名
     */
    fun getFileName(file: File?): String? {
        if (file == null) return null
        return getFileName(file.path)
    }

    /**
     * 获取全路径中的文件名
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    fun getFileName(filePath: String): String {
        if (StringUtils.isBlank(filePath)) return filePath
        val lastSep = filePath.lastIndexOf(File.separator)
        return if (lastSep == -1) filePath else filePath.substring(lastSep + 1)
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param file 文件
     * @return 不带拓展名的文件名
     */
    fun getFileNameNoExtension(file: File?): String? {
        if (file == null) return null
        return getFileNameNoExtension(file.path)
    }

    /**
     * 获取全路径中的不带拓展名的文件名
     *
     * @param filePath 文件路径
     * @return 不带拓展名的文件名
     */
    fun getFileNameNoExtension(filePath: String): String {
        if (StringUtils.isBlank(filePath)) return filePath
        val lastPoi = filePath.lastIndexOf('.')
        val lastSep = filePath.lastIndexOf(File.separator)
        if (lastSep == -1) {
            return if (lastPoi == -1) filePath else filePath.substring(0, lastPoi)
        }
        if (lastPoi == -1 || lastSep > lastPoi) {
            return filePath.substring(lastSep + 1)
        }
        return filePath.substring(lastSep + 1, lastPoi)
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param file 文件
     * @return 文件拓展名
     */
    fun getFileExtension(file: File?): String? {
        if (file == null) return null
        return getFileExtension(file.path)
    }

    /**
     * 获取全路径中的文件拓展名
     *
     * @param filePath 文件路径
     * @return 文件拓展名
     */
    fun getFileExtension(filePath: String): String {
        if (StringUtils.isBlank(filePath)) return filePath
        val lastPoi = filePath.lastIndexOf('.')
        val lastSep = filePath.lastIndexOf(File.separator)
        if (lastPoi == -1 || lastSep >= lastPoi) return ""
        return filePath.substring(lastPoi + 1)
    }

    private val TAG = "FileUtil"

    val CONTENT_TYPE_LOG = "log"
    val CONTENT_TYPE_BUG = "bug"
    val CONTENT_TYPE_CACHE = "cache"
    /** LOG文件的扩展名  */
    val EXTENSION_LOG = ".log"

    /** BUG文件的扩展名  */
    val EXTENSION_BUG = ".cr"


    /**
     * 日志写入文件
     *
     * @param message
     * @param tr
     */
    fun writeLogToFile(message: String, tr: Throwable) {
        val sb = StringBuffer()
        sb.append(DateUtils.nowTimeString)
        sb.append(message).append("\n")
        sb.append(getStackTraceString(tr))
        try {
            createFileByType(CONTENT_TYPE_LOG, sb.toString())
            deleteLogFile(SDCardUtils.defaultAppPath + "/log")
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file ", e)
        }

    }


    /**
     * 异常写入文件
     *
     * @param crashinfo
     * @param tr
     */
    fun writeBugToFile(crashinfo: String, tr: Throwable) {
        val sb = StringBuffer()
        sb.append(crashinfo).append(getStackTraceString(tr))

        try {
            createFileByType(CONTENT_TYPE_BUG, sb.toString())
        } catch (e: Exception) {
            Log.e(TAG, "an error occured while writing file ", e)
        }

    }

    /**
     * 写入文件
     *
     * @param type
     * @param content
     */
    fun createFileByType(type: String, content: String) {
        var fileName: String? = null
        var fileDir: String? = null

        if (CONTENT_TYPE_LOG == type) {
            fileName = DateUtils.millis2String(DateUtils.nowTimeMills, DateUtils.DATE_FORMAT_LOG_CONTENT) + EXTENSION_LOG
            fileDir = createFileDir(CONTENT_TYPE_LOG).toString()
        } else if (CONTENT_TYPE_BUG == type) {
            fileName = "bug-" + DateUtils.millis2String(DateUtils.nowTimeMills, DateUtils.DATE_FORMAT_BUG_CONTENT) + EXTENSION_BUG
            fileDir = createFileDir(CONTENT_TYPE_BUG).toString()
        }

        if (null != fileName && null != fileDir) {
            val file = createFile(fileDir, fileName)
            writeToFile(file, content)
        }
    }


    /**
     * 获取错误报告文件名
     *
     * @return
     */
    // 实现FilenameFilter接口的类实例可用于过滤器文件名
    // 测试指定文件是否应该包含在某一文件列表中。
    // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录
    val bugFiles: Array<String>
        get() {
            val bugDir = createFileDir(CONTENT_TYPE_BUG)
            Log.w(TAG, "BugDir = " + bugDir.absolutePath)
            val filter = FilenameFilter { dir, name -> name.endsWith(EXTENSION_BUG) }
            return bugDir.list(filter)
        }

    /**
     * 只保留最近10次记录文件
     *
     * @param path
     */
    fun deleteLogFile(path: String) {
        val allFiles = File(path).listFiles()
        var temp: File
        if (allFiles.size > 10) {
            for (i in allFiles.indices) {
                for (j in i + 1..allFiles.size - 1) {
                    if (allFiles[i].lastModified() < allFiles[j].lastModified()) {
                        temp = allFiles[i]
                        allFiles[i] = allFiles[j]
                        allFiles[j] = temp
                    }
                }
                allFiles[allFiles.size - 1].delete()
            }
        }
    }

    /**
     * writeToFile
     *
     * @param file
     * @param content
     */
    fun writeToFile(file: File?, content: String) {

        if (file != null) {
            try {
                val fw = FileWriter(file, true)
                val bw = BufferedWriter(fw)
                bw.append(content)
                bw.flush()
                bw.close()
                fw.close()
            } catch (e: IOException) {
                Log.e(TAG, "writeToFile() error ", e)
            }

        } else {
            Log.i(TAG, "writeToFile() file is null ")
        }

    }

    /**
     * 根据传递的的目录名称创建目录
     *
     * @param fileDirName
     * @return
     */
    fun createFileDir(fileDirName: String): File {
        val path = SDCardUtils.defaultAppPath + File.separator + fileDirName
        val fileDir = File(path)
        //创建文件目录
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        return fileDir
    }

    fun createFile(fileDir: String, fileName: String): File? {
        val dir = File(fileDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(fileDir + File.separator + fileName)
        if (!file.exists()) {
            try {
                file.createNewFile()
            } catch (e: IOException) {
                Log.e(TAG, "createFile() error ", e)
                return null
            }

        }
        return file
    }

    /**
     * getStackTraceString
     * @param tr
     * @return
     */
    fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t.printStackTrace(pw)
            t = t.cause
        }
        pw.flush()
        pw.close()
        return sw.toString()
    }

}