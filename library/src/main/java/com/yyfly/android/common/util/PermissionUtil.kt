package com.yyfly.android.common.util

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.yyfly.android.common.ui.activity.ShadowActivity
import java.util.*


/**
 * 权限申请工具，使用基于Queue的请求队列实现，如果同时有几个地方进行权限申请
 * 那么 按照FIFO 规则进行权限申请，请尽量避免在异步代码中申请权限，否则
 * 可能影响代码的正确执行
 *
 *
 * 必要申请的运行时权限
 * CALENDAR组
 * android.permission.READ_CALENDAR
 * android.permission.WRITE_CALENDAR
 * CAMERA组
 * android.permission.CAMERA
 * CONTACTS组
 * android.permission.READ_CONTACTS
 * android.permission.WRITE_CONTACTS
 * android.permission.GET_ACCOUNTS
 * LOCATION组
 * android.permission.ACCESS_FINE_LOCATION
 * android.permission.ACCESS_COARSE_LOCATION
 * MICROPHONE组
 * android.permission.RECORD_AUDIO
 * PHONE组
 * android.permission.READ_PHONE_STATE
 * android.permission.CALL_PHONE
 * android.permission.READ_CALL_LOG
 * android.permission.WRITE_CALL_LOG
 * android.permission.ADD_VOICEMAIL
 * android.permission.USE_SIP
 * android.permission.PROCESS_OUTGOING_CALLS
 * SENSORS组
 * android.permission.BODY_SENSORS
 * SMS组
 * android.permission.SEND_SMS
 * android.permission.RECEIVE_SMS
 * android.permission.READ_SMS
 * android.permission.RECEIVE_WAP_PUSH
 * android.permission.RECEIVE_MMS
 * STORAGE组
 * android.permission.READ_EXTERNAL_STORAGE
 * android.permission.WRITE_EXTERNAL_STORAGE
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
class PermissionUtil private constructor() {

    private var mCurrentPermissionReq: PermissionReq? = null
    private var mActivity: AppCompatActivity? = null

    private object LazyHolder {
        var permissionUtil = PermissionUtil()
    }


    /**
     * 申请权限
     *
     * @param context     the context
     * @param permissions 请求权限列表
     * *                    此列表只需要传权限所在组中的任何一个
     * *                    android 权限申请中，只要权限组中任意一个被授权，那么该
     * *                    组中所有权限都会被授权。
     * *                    比如需要同时申请 存储权限和相机权限
     * *                    只需要传 new String[]{Manifest.permission.CAMERA,Manifest.permission.CAMERA,READ_EXTERNAL_STORAGE}
     * *                    即可。当然，如果你想全部传，也无所谓了
     * *
     * @param listener    权限申请 监听
     */
    fun reqPermissions(context: Context, permissions: Array<String>, listener: OnReqPermissionListener?) {
        if (null == listener) {
            throw IllegalArgumentException("onReqPermissionListener 不能为空")
        }
        //android M 以下 默认拥有权限
        if (!isVersionGtM) {
            listener.onSuccess()
            return
        }

        //如果用户已经禁止权限（而且打勾），则直接反馈
        //        String[] alreadyDeniedPermissions = getAlreadyDeniedPermissions(context, permissions);
        //        if (null != alreadyDeniedPermissions) {
        //            listener.onAlreadyDenied(alreadyDeniedPermissions);
        //            return;
        //        }
        //取得没有授权的权限列表
        val notGrantedP = HashSet<String>()
        for (permission in permissions) {
            if (!isGranted(context, permission)) {
                notGrantedP.add(permission)
            }
        }

        if (notGrantedP.size < 1) {
            //全部已经授权 直接返回
            listener.onSuccess()
        } else {
            //构造权限请求
            mCurrentPermissionReq = PermissionReq(permissions, context, listener)
            val intent = Intent(mCurrentPermissionReq!!.context, ShadowActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("permissions", mCurrentPermissionReq!!.permissions)
            mCurrentPermissionReq!!.context.startActivity(intent)
        }
    }


    /**
     * 用户取消了权限申请
     */
    fun userCancel() {
        if (null != mCurrentPermissionReq) {
            mCurrentPermissionReq!!.listener.onUserCancel()
            //下一个
        }
    }

    /**
     * 授权结果
     *
     * @param reqPermissions 申请的权限列表
     * @param reqResult      授权结果
     */
    fun grantedResult(reqPermissions: Array<String>, reqResult: IntArray) {
        //授权列表
        val grantedP = HashSet<String>()
        //拒绝列表
        val notGrantedP = HashSet<String>()
        for (i in reqResult.indices) {
            if (reqResult[i] == PackageManager.PERMISSION_GRANTED) {
                grantedP.add(reqPermissions[i])
            } else {
                notGrantedP.add(reqPermissions[i])
            }
        }
        if (notGrantedP.isEmpty()) {
            //拒绝为空 则表示权限申请成功
            if (null != mCurrentPermissionReq) {
                mCurrentPermissionReq!!.listener.onSuccess()
            }
        } else {
            //权限申请失败，存在被拒绝项
            if (null != mCurrentPermissionReq) {
                mCurrentPermissionReq!!.listener.onFailed(grantedP.toTypedArray(), notGrantedP.toTypedArray())
            }
        }
    }

    /**
     * 判断是否已经被授权
     *
     * @param context
     * @param permission
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun isGranted_(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 判断是否已经被授权
     *
     * @param context    the context
     * @param permission the permission
     * @return boolean
     */
    private fun isGranted(context: Context, permission: String): Boolean {
        return !isVersionGtM || isGranted_(context, permission)
    }


    /**
     * 判断当前操作系统版本是否是 6.0以上
     *
     * @return the boolean
     */
    private val isVersionGtM: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


    /**
     * 判断权限列表中那些已经被用户禁止掉，而且选择了不在提醒

     * @param permissions
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    private fun getAlreadyDeniedPermissions(context: Context, permissions: Array<String>): Array<String>? {
        if (mActivity == null) {
            return null
        }
        val temp = HashSet<String>()
        for (permission in permissions) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(mActivity!!, permission) && !isGranted(context, permission)) {
                temp.add(permission)
            }
        }
        if (temp.isEmpty()) {
            return null
        }
        return temp.toTypedArray()
    }


    /**
     * @param activity    the activity
     * @param permissions the permissions
     * @return the boolean
     */
    @TargetApi(Build.VERSION_CODES.M)
    @Synchronized fun shouldShowPermissionRequestTip(activity: AppCompatActivity, permissions: Array<String>): Boolean {
        if (activity !is ShadowActivity) {
            throw IllegalArgumentException("只能在 ShadowActivity 中调用")
        }
        if (null == mActivity) {
            mActivity = activity
        }
        var flag = false

        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity!!, permission)) {
                flag = true
                break
            }
        }
        return flag
    }

    /**
     * 权限请求
     */
    private class PermissionReq
    /**
     * Permission req.
     *
     * @param permissions the permissions
     * @param context     the context
     * @param listener    the listener
     */
    (
            /**
             * 请求权限列表
             *
             * @return the string [ ]
             */
            val permissions: Array<String>,
            /**
             * 请求所在context
             *
             * @return the context
             */
            val context: Context,
            /**
             * 请求回调
             *
             * @return the on req permission listener
             */
            val listener: OnReqPermissionListener)


    /**
     * 权限申请回掉函数
     */
    interface OnReqPermissionListener {
        /**
         * 用户取消掉授权动作
         */
        fun onUserCancel()

        /**
         * 所有权限授权成功
         */
        fun onSuccess()

        /**
         * 授权失败，即申请的权限中有一项或者多项被拒绝
         *
         * @param notGrantedPermission 被拒绝的权限
         * @param grantedPermission    用户授权的权限
         */
        fun onFailed(notGrantedPermission: Array<String>, grantedPermission: Array<String>)

        /**
         * 已经被禁止
         *
         * @param alreadyDeniedPermissions 已经被禁止的 权限列表
         */
        fun onAlreadyDenied(alreadyDeniedPermissions: Array<String>)
    }

    companion object {

        /**
         * Get instance permission utils.
         *
         * @return the permission utils
         */
        val instance: PermissionUtil
            get() = LazyHolder.permissionUtil
    }

}
