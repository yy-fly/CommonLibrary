package com.yyfly.android.common.ui.activity

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.yyfly.android.common.R
import com.yyfly.android.common.util.PermissionUtil

/**
 * 权限申请代理 Activity,替所有需要权限申请的地方进行权限申请工作
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
@TargetApi(Build.VERSION_CODES.M)
class ShadowActivity : AppCompatActivity() {

    private var permissions: Array<String>? = null
    private val REQUEST_CODE = 44

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        permissions = intent.getStringArrayExtra("permissions")
        println("开始授权")
        if (PermissionUtil.instance.shouldShowPermissionRequestTip(this, permissions!!)) {
            showTip()
        } else {
            ActivityCompat.requestPermissions(this@ShadowActivity, permissions!!, REQUEST_CODE)
        }
    }

    private fun showTip() {
        val dialog = AlertDialog.Builder(this, R.style.PermissionReqTipDialog)
                .setTitle("需要权限")
                .setMessage("如果您需要使用此功能,请您提供权限,即在后面的对话框中点击确认按钮授权。")
                .setNegativeButton("以后再说") { dialog, which ->
                    finish()
                    PermissionUtil.instance.userCancel()
                }
                .setOnCancelListener {
                    finish()
                    PermissionUtil.instance.userCancel()
                }
                .setPositiveButton("确认") { dialog, which -> ActivityCompat.requestPermissions(this@ShadowActivity, permissions!!, REQUEST_CODE) }
                .setCancelable(false)
                .show()
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#212121"))
        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00b0ff"))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            this.finish()
            PermissionUtil.instance.grantedResult(permissions, grantResults)
        }
    }

}
