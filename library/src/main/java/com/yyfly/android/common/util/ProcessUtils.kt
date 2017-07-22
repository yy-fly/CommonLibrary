package com.yyfly.android.common.util

import android.app.ActivityManager
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import com.yyfly.android.common.App
import java.util.*


/**
 * 进程相关工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object ProcessUtils {

    /**
     * 获取前台线程包名
     * 当不是查看当前App，且SDK大于21时，
     * 需添加权限 `<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"/>`
     *
     * @return 前台应用包名
     */
    // 有"有权查看使用权限的应用"选项
    val foregroundProcessName: String?
        get() {
            val manager = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val infos = manager.runningAppProcesses
            infos?.let {
                it.forEach {
                    if (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return it.processName
                    }
                }
            }
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
                val packageManager = App.context.packageManager
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                println(list)
                if (list.size > 0) {
                    try {
                        val info = packageManager.getApplicationInfo(App.context.getPackageName(), 0)
                        val aom = App.context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                        if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) != AppOpsManager.MODE_ALLOWED) {
                            App.context.startActivity(intent)
                        }
                        if (aom.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) != AppOpsManager.MODE_ALLOWED) {
                            Log.d("getForegroundApp", "没有打开\"有权查看使用权限的应用\"选项")
                            return null
                        }
                        val usageStatsManager = App.context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
                        val endTime = System.currentTimeMillis()
                        val beginTime = endTime - 86400000 * 7
                        val usageStatses = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime, endTime)
                        if (usageStatses == null || usageStatses.isEmpty()) return null
                        var recentStats: UsageStats? = null
                        for (usageStats in usageStatses) {
                            if (recentStats == null || usageStats.lastTimeUsed > recentStats.lastTimeUsed) {
                                recentStats = usageStats
                            }
                        }
                        return if (recentStats == null) null else recentStats.packageName
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }

                } else {
                    Log.d("getForegroundApp", "无\"有权查看使用权限的应用\"选项")
                }
            }
            return null
        }

    /**
     * 获取后台服务进程
     * 需添加权限 `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>`
     *
     * @return 后台服务进程
     */
    val allBackgroundProcesses: Set<String>
        get() {
            val am = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val infos = am.runningAppProcesses
            val set = HashSet<String>()
            infos.forEach {
                Collections.addAll(set, *it.pkgList)
            }
            return set
        }

    /**
     * 杀死所有的后台服务进程
     * 需添加权限 `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>`
     *
     * @return 被暂时杀死的服务集合
     */
    fun killAllBackgroundProcesses(): Set<String> {
        val am = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var infos: List<ActivityManager.RunningAppProcessInfo> = am.runningAppProcesses
        val set = HashSet<String>()
        infos.forEach {
            it.pkgList?.forEach {
                am.killBackgroundProcesses(it)
                set.add(it)
            }
        }
        infos = am.runningAppProcesses

        infos.forEach {
            it.pkgList?.forEach {
                set.remove(it)
            }
        }
        return set
    }

    /**
     * 杀死后台服务进程
     * 需添加权限 `<uses-permission android:name="android.permission.KILL_BACKGROUND_PROCESSES"/>`
     *
     * @param packageName 包名
     * @return `true`: 杀死成功<br></br>`false`: 杀死失败
     */
    fun killBackgroundProcesses(packageName: String): Boolean {
        if (StringUtils.isBlank(packageName)) return false
        val am = App.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var infos: List<ActivityManager.RunningAppProcessInfo>? = am.runningAppProcesses
        if (ListUtils.isEmpty(infos)) return true
        infos?.forEach {
            if (Arrays.asList(*it.pkgList).contains(packageName)) {
                am.killBackgroundProcesses(packageName)
            }

        }
        infos = am.runningAppProcesses
        if (ListUtils.isEmpty(infos)) return true
        infos?.forEach {
            if (Arrays.asList(*it.pkgList).contains(packageName)) {
                return false
            }
        }
        return true
    }
}
