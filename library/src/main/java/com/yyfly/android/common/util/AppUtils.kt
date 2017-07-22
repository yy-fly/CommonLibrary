package com.yyfly.android.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.Signature
import android.graphics.drawable.Drawable
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import com.yyfly.android.common.App
import java.io.File
import java.util.*


/**
 * 应用工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object AppUtils {

    private val TAG = AppUtils::class.java.simpleName

    /**
     * 获得应用名称
     *
     * @return
     */
    val applicationName: String?
        get() {
            val pm = App.context.packageManager
            val applicationName = App.context.applicationInfo.loadLabel(pm) as String
            if (StringUtils.isNotBlank(applicationName)) {
                return applicationName
            }
            return null
        }
    /**
     * 获得mac
     *
     * @return
     */
    val mac: String?
        get() {
            if (null != macAddress) {
                return macAddress!!.replace(":".toRegex(), "-").toUpperCase(Locale.getDefault())
            }
            return null
        }

    //必须先打开，才能获取到MAC地址
    val macAddress: String?
        get() {
            val wifiManager = App.context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

            if (!wifiManager.isWifiEnabled) {
                wifiManager.isWifiEnabled = true
                wifiManager.isWifiEnabled = false
            }
            val info = wifiManager.connectionInfo
            if (null != info) {
                if (null != info.macAddress) {
                    return info.macAddress
                }
            }
            return null
        }

    /**
     * 获得versionName
     *
     * @return
     */
    val versionName: String?
        get() {
            var versionName: String? = null
            try {
                val pm = App.context.packageManager
                val pi = pm.getPackageInfo(App.context.packageName, PackageManager.GET_ACTIVITIES)
                if (pi != null) {
                    versionName = pi.versionName
                }
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
                Log.i(TAG, "getVersionName()", e)
            }

            return versionName
        }

    /**
     * 获得VersionCode
     *
     * @return
     */
    val versionCode: String?
        get() {
            var versionCode: String? = null
            try {
                val pm = App.context.packageManager
                val pi = pm.getPackageInfo(App.context.packageName, PackageManager.GET_ACTIVITIES)
                if (pi != null) {
                    versionCode = pi.versionCode.toString() + ""
                }
            } catch (e: NameNotFoundException) {
                e.printStackTrace()
                Log.i(TAG, "getVersionCode()", e)
            }

            return versionCode
        }

    /**
     * 获取设备ID
     * 如果imei为空
     * 获取mac地址
     * 如果mac地址不为空
     * @return
     */
    val identifier: String
        get() {
            var deviceId = ""
            if (StringUtils.isBlank(SPUtils.getString(ConstUtils.SP_IDENTIFIER))) {
                var identifier: String? = AppUtils.imei
                if (StringUtils.isBlank(identifier)) {
                    val mac = macAddress
                    if (StringUtils.isNotBlank(mac)) {
                        identifier = macAddress!!.replace(":".toRegex(), "")
                    } else {
                        identifier = AppUtils.uniquePseudoID
                    }
                }
                deviceId = MD5.GetMD5Code(identifier!!)!!
                SPUtils.setString(ConstUtils.SP_IDENTIFIER, deviceId)
            } else {
                deviceId = SPUtils.getString(ConstUtils.SP_IDENTIFIER)
            }

            Log.i(TAG, "device id = " + deviceId)
            return deviceId
        }

    /**
     * 获得imei
     *
     * @return
     */
    val imei: String?
        get() {
            val tm = App.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (tm.deviceId != null && "" != tm.deviceId) {
                return tm.deviceId.toLowerCase(Locale.getDefault())
            }
            return null
        }

    /**
     *  If all else fails, if the user does have lower than API 9 (lower
     * than Gingerbread), has reset their phone or 'Secure.ANDROID_ID'
     * returns 'null', then simply the ID returned will be solely based
     * off their Android device information. This is where the collisions
     * can happen.
     * Thanks http://www.pocketmagic.net/?p=1662!
     * Try not to use DISPLAY, HOST or ID - these items could change.
     * If there are collisions, there will be overlapping data
     * Thanks to @Roman SL!
     * http://stackoverflow.com/a/4789483/950427
     * Only devices with API >= 9 have android.os.Build.SERIAL
     * http://developer.android.com/reference/android/os/Build.html#SERIAL
     * If a user upgrades software or roots their phone, there will be a duplicate entry
     * Go ahead and return the serial for api => 9
     * String needs to be initialized
     * some value
     * Thanks @Joe!
     * http://stackoverflow.com/a/2853253/950427
     * Finally, combine the values we have found by using the UUID class to create a unique identifier
     * @return
     */
    val uniquePseudoID: String
        get() {
            val m_szDevIDShort = "35" +
                    Build.BOARD.length % 10 +
                    Build.BRAND.length % 10 +
                    Build.CPU_ABI.length % 10 +
                    Build.DEVICE.length % 10 +
                    Build.MANUFACTURER.length % 10 +
                    Build.MODEL.length % 10 +
                    Build.PRODUCT.length % 10
            var serial: String? = null
            try {
                serial = Build::class.java.getField("SERIAL").get(null).toString()
                return UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString().replace("-".toRegex(), "")
            } catch (exception: Exception) {
                serial = "serial"
            }

            return UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString().replace("-".toRegex(), "")
        }

    /**
     * 判断App是否有root权限
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    val isAppRoot: Boolean
        get() {
            val result = ShellUtils.execCmd("echo root", true)
            if (result.result == 0) {
                return true
            }
            if (result.errorMsg != null) {
                Log.d("isAppRoot", result.errorMsg)
            }
            return false
        }

    /**
     * 根据包名获得应用名称
     *
     * @param packageName
     * @return
     */
    fun getLabelByPackageName(packageName: String): String? {
        try {
            val appInfo = App.context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA + PackageManager.GET_UNINSTALLED_PACKAGES)
            return appInfo.loadLabel(App.context.packageManager) as String
        } catch (e: NameNotFoundException) {
        }

        return null
    }

    /**
     * 根据包名获取icon
     *
     * @param packageName
     * @return
     */
    fun getIconByPackageName(packageName: String): Drawable? {
        try {
            val appInfo = App.context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA + PackageManager.GET_UNINSTALLED_PACKAGES)
            return appInfo.loadIcon(App.context.packageManager)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 根据包名获取组件
     *
     * @param packageName
     * @return
     */
    fun getComponentNameByPackageName(packageName: String): ComponentName? {
        val intent = App.context.packageManager.getLaunchIntentForPackage(packageName)
        if (null != intent) {
            val cn = intent.component
            if (null != cn) {
                return cn
            }
        }
        return null
    }

    /**
     * 根据包名获取类名
     *
     * @param packageName
     * @return
     */
    fun getClassNameByPackageName(packageName: String): String {
        val cn = getComponentNameByPackageName(packageName)
        if (null != cn) {
            return cn.className
        }
        return ""
    }

    /**
     * 判断应用是否安装
     *
     * @param packageName
     * @return
     */
    fun isAppInstalled(packageName: String): Boolean {
        if (StringUtils.isEmpty(packageName)) {
            return false
        }
        var app: ApplicationInfo? = null
        try {
            app = App.context.packageManager.getApplicationInfo(packageName, 0)
        } catch (e: NameNotFoundException) {
            Log.e(TAG, " isAppInstalled ", e)
        }

        return app != null
    }

    /**
     * 判断App是否安装

     * @param context     上下文
     * *
     * @param packageName 包名
     * *
     * @return `true`: 已安装<br></br>`false`: 未安装
     */
    fun isInstallApp(context: Context, packageName: String): Boolean {
        return !StringUtils.isBlank(packageName) && IntentUtils.getLaunchAppIntent(packageName) != null
    }

    /**
     * 安装App（支持6.0）

     * @param context 上下文
     * *
     * @param file    文件
     */
    fun installApp(context: Context, file: File) {
        if (!FileUtils.isFileExists(file)) return
        context.startActivity(IntentUtils.getInstallAppIntent(file))
    }


    /**
     * 安装App(支持6.0)
     *
     * @param activity    activity
     * @param file        文件
     * @param requestCode 请求值
     */
    fun installApp(activity: Activity, file: File, requestCode: Int) {
        if (!FileUtils.isFileExists(file)) return
        activity.startActivityForResult(IntentUtils.getInstallAppIntent(file), requestCode)
    }

    /**
     * 静默安装App
     * 非root需添加权限 `<uses-permission android:name="android.permission.INSTALL_PACKAGES" />`
     *
     * @param filePath 文件路径
     * @return `true`: 安装成功<br></br>`false`: 安装失败
     */
    fun installAppSilent(filePath: String): Boolean {
        val file = FileUtils.getFileByPath(filePath)
        if (!FileUtils.isFileExists(file)) return false
        val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm install " + filePath
        val commandResult = ShellUtils.execCmd(command, !isSystemApp(App.context), true)
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success")
    }

    /**
     * 卸载App
     *
     * @param context     上下文
     * @param packageName 包名
     */
    fun uninstallApp(context: Context, packageName: String) {
        if (StringUtils.isBlank(packageName)) return
        context.startActivity(IntentUtils.getUninstallAppIntent(packageName))
    }

    /**
     * 卸载App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    fun uninstallApp(activity: Activity, packageName: String, requestCode: Int) {
        if (StringUtils.isBlank(packageName)) return
        activity.startActivityForResult(IntentUtils.getUninstallAppIntent(packageName), requestCode)
    }

    /**
     * 静默卸载App
     * 非root需添加权限 `<uses-permission android:name="android.permission.DELETE_PACKAGES" />`
     *
     * @param context     上下文
     * @param packageName 包名
     * @param isKeepData  是否保留数据
     * @return `true`: 卸载成功<br></br>`false`: 卸载成功
     */
    fun uninstallAppSilent(context: Context, packageName: String, isKeepData: Boolean): Boolean {
        if (StringUtils.isBlank(packageName)) return false
        val command = "LD_LIBRARY_PATH=/vendor/lib:/system/lib pm uninstall " + (if (isKeepData) "-k " else "") + packageName
        val commandResult = ShellUtils.execCmd(command, !isSystemApp(context), true)
        return commandResult.successMsg != null && commandResult.successMsg.toLowerCase().contains("success")
    }

    /**
     * 打开App
     *
     * @param packageName 包名
     */
    fun launchApp(packageName: String) {
        if (StringUtils.isBlank(packageName)) return
        App.context.startActivity(IntentUtils.getLaunchAppIntent(packageName))
    }

    /**
     * 打开App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    fun launchApp(activity: Activity, packageName: String, requestCode: Int) {
        if (StringUtils.isBlank(packageName)) return
        activity.startActivityForResult(IntentUtils.getLaunchAppIntent(packageName), requestCode)
    }

    /**
     * 获取App包名
     *
     * @param context 上下文
     * @return App包名
     */
    fun getAppPackageName(context: Context): String {
        return context.packageName
    }

    /**
     * 获取App具体设置
     *
     * @param context     上下文
     * @param packageName 包名
     */
    @JvmOverloads fun getAppDetailsSettings(context: Context, packageName: String = context.packageName) {
        if (StringUtils.isBlank(packageName)) return
        context.startActivity(IntentUtils.getAppDetailsSettingsIntent(packageName))
    }

    /**
     * 获取App名称
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App名称
     */
    @JvmOverloads fun getAppName(context: Context, packageName: String = context.packageName): String? {
        if (StringUtils.isBlank(packageName)) return null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.loadLabel(pm)?.toString()
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获取App图标
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App图标
     */
    @JvmOverloads fun getAppIcon(context: Context, packageName: String = context.packageName): Drawable? {
        if (StringUtils.isBlank(packageName)) return null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.loadIcon(pm)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获取App路径
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App路径
     */
    @JvmOverloads fun getAppPath(context: Context, packageName: String = context.packageName): String? {
        if (StringUtils.isBlank(packageName)) return null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.applicationInfo?.sourceDir
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获取App版本号
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本号
     */
    @JvmOverloads fun getAppVersionName(context: Context, packageName: String = context.packageName): String? {
        if (StringUtils.isBlank(packageName)) return null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.versionName
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获取App版本码
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App版本码
     */
    @JvmOverloads fun getAppVersionCode(context: Context, packageName: String = context.packageName): Int {
        if (StringUtils.isBlank(packageName)) return -1
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return pi?.versionCode ?: -1
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return -1
        }

    }

    /**
     * 判断App是否是系统应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmOverloads fun isSystemApp(context: Context, packageName: String = context.packageName): Boolean {
        if (StringUtils.isBlank(packageName)) return false
        try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            return ai != null && ai.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 判断App是否是Debug版本
     *
     * @param context     上下文
     * @param packageName 包名
     * @return `true`: 是<br></br>`false`: 否
     */
    @JvmOverloads fun isAppDebug(context: Context, packageName: String = context.packageName): Boolean {
        if (StringUtils.isBlank(packageName)) return false
        try {
            val pm = context.packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            return ai != null && ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 获取App签名
     *
     * @param context 上下文
     * @return App签名
     */
    fun getAppSignature(context: Context): Array<Signature>? {
        return getAppSignature(context, context.packageName)
    }

    /**
     * 获取App签名
     *
     * @param context     上下文
     * @param packageName 包名
     * @return App签名
     */
    @SuppressLint("PackageManagerGetSignatures")
    fun getAppSignature(context: Context, packageName: String): Array<Signature>? {
        if (StringUtils.isBlank(packageName)) return null
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            return pi?.signatures
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 获取应用签名的的SHA1值
     * 可据此判断高德，百度地图key是否正确
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 应用签名的SHA1字符串, 比如：53:FD:54:DC:19:0F:11:AC:B5:22:9E:F1:1A:68:88:1B:8B:E8:54:42
     */
    @JvmOverloads fun getAppSignatureSHA1(context: Context, packageName: String = context.packageName): String? {
        val signature = getAppSignature(context, packageName) ?: return null
        return EncryptUtils.encryptSHA1ToString(signature[0].toByteArray()).replace("(?<=[0-9A-F]{2})[0-9A-F]{2}".toRegex(), ":$0")
    }

    /**
     * 判断App是否处于前台
     *
     * @param context 上下文
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isAppForeground(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val infos = manager.runningAppProcesses
        if (infos == null || infos.size == 0) return false
        for (info in infos) {
            if (info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return info.processName == context.packageName
            }
        }
        return false
    }

    /**
     * 判断App是否处于前台
     *
     * 当不是查看当前App，且SDK大于21时，
     * 需添加权限 `<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"/>`
     *
     * @param context     上下文
     * @param packageName 包名
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isAppForeground(context: Context, packageName: String): Boolean {
        return !StringUtils.isBlank(packageName) && packageName == ProcessUtils.foregroundProcessName
    }

    /**
     * 封装App信息的Bean类
     */
    class AppInfo
    /**
     * @param name        名称
     * @param icon        图标
     * @param packageName 包名
     * @param packagePath 包路径
     * @param versionName 版本号
     * @param versionCode 版本码
     * @param isSystem    是否系统应用
     */
    (packageName: String, name: String, icon: Drawable, packagePath: String,
     versionName: String, versionCode: Int, isSystem: Boolean) {

        var name: String? = null
        var icon: Drawable? = null
        var packageName: String? = null
        var packagePath: String? = null
        var versionName: String? = null
        var versionCode: Int = 0
        var isSystem: Boolean = false

        init {
            this.name = name
            this.icon = icon
            this.packageName = packageName
            this.packagePath = packagePath
            this.versionName = versionName
            this.versionCode = versionCode
            this.isSystem = isSystem
        }

        override fun toString(): String {
            return "App包名：" + packageName +
                    "\nApp名称：" + name +
                    "\nApp图标：" + icon +
                    "\nApp路径：" + packagePath +
                    "\nApp版本号：" + versionName +
                    "\nApp版本码：" + versionCode +
                    "\n是否系统App：" + isSystem
        }
    }

    /**
     * 获取App信息
     *
     * AppInfo（名称，图标，包名，版本号，版本Code，是否系统应用）
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 当前应用的AppInfo
     */
    @JvmOverloads fun getAppInfo(context: Context, packageName: String = context.packageName): AppInfo? {
        try {
            val pm = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            return getBean(pm, pi)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    private fun getBean(pm: PackageManager?, pi: PackageInfo?): AppInfo? {
        if (pm == null || pi == null) return null
        val ai = pi.applicationInfo
        val packageName = pi.packageName
        val name = ai.loadLabel(pm).toString()
        val icon = ai.loadIcon(pm)
        val packagePath = ai.sourceDir
        val versionName = pi.versionName
        val versionCode = pi.versionCode
        val isSystem = ApplicationInfo.FLAG_SYSTEM and ai.flags != 0
        return AppInfo(packageName, name, icon, packagePath, versionName, versionCode, isSystem)
    }

    /**
     * 获取所有已安装App信息
     * [.getBean]（名称，图标，包名，包路径，版本号，版本Code，是否系统应用）
     * 依赖上面的getBean方法
     *
     * @param context 上下文
     * @return 所有已安装的AppInfo列表
     */
    fun getAppsInfo(context: Context): List<AppInfo> {
        val list = ArrayList<AppInfo>()
        val pm = context.packageManager
        // 获取系统中安装的所有软件信息
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val ai = getBean(pm, pi) ?: continue
            list.add(ai)
        }
        return list
    }

    /**
     * 清除App所有数据
     *
     * @param context  上下文
     * @param dirPaths 目录路径
     * @return `true`: 成功<br></br>`false`: 失败
     */
    fun cleanAppData(context: Context, vararg dirPaths: String): Boolean {
        val dirs = arrayOfNulls<File>(dirPaths.size)
        var i = 0
        for (dirPath in dirPaths) {
            dirs[i++] = File(dirPath)
        }
        return cleanAppData(*dirs)
    }

    /**
     * 清除App所有数据
     *
     * @param dirs 目录
     * @return `true`: 成功<br></br>`false`: 失败
     */
    fun cleanAppData(vararg dirs: File?): Boolean {
        var isSuccess = CleanUtils.cleanInternalCache()
        isSuccess = isSuccess and CleanUtils.cleanInternalDbs()
        isSuccess = isSuccess and CleanUtils.cleanInternalSP()
        isSuccess = isSuccess and CleanUtils.cleanInternalFiles()
        isSuccess = isSuccess and CleanUtils.cleanExternalCache()
        for (dir in dirs) {
            isSuccess = isSuccess and CleanUtils.cleanCustomCacheFile(dir!!)
        }
        return isSuccess
    }

    /**
     * 关闭应用
     */
    fun exitApp() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        App.context.startActivity(startMain)
        System.exit(0)
    }

}