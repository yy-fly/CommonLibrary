package com.yyfly.android.common.util

import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.webkit.MimeTypeMap
import com.yyfly.android.common.App

import java.io.File


/**
 * Intent工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object IntentUtils {

    /**
     * 获取安装App（支持6.0）的意图
     *
     * @param filePath 文件路径
     * @return intent
     */
    fun getInstallAppIntent(filePath: String): Intent? {
        return getInstallAppIntent(FileUtils.getFileByPath(filePath))
    }

    /**
     * 获取安装App(支持6.0)的意图
     *
     * @param file 文件
     * @return intent
     */
    fun getInstallAppIntent(file: File?): Intent? {
        if (file == null) return null
        val intent = Intent(Intent.ACTION_VIEW)
        val type: String

        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive"
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(App.context, "com.your.package.fileProvider", file)
            intent.setDataAndType(contentUri, type)
        }
        intent.setDataAndType(Uri.fromFile(file), type)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取卸载App的意图
     *
     * @param packageName 包名
     * @return intent
     */
    fun getUninstallAppIntent(packageName: String): Intent {
        val intent = Intent(Intent.ACTION_DELETE)
        intent.data = Uri.parse("package:" + packageName)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取打开App的意图
     *
     * @param packageName 包名
     * @return intent
     */
    fun getLaunchAppIntent(packageName: String): Intent {
        return App.context.getPackageManager().getLaunchIntentForPackage(packageName)
    }

    /**
     * 打开浏览器意图
     *
     * @param url
     * @return
     */
    fun getBrowserIntent(url: String): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse(url)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取App具体设置的意图
     *
     * @param packageName 包名
     * @return intent
     */
    fun getAppDetailsSettingsIntent(packageName: String): Intent {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
        intent.data = Uri.parse("package:" + packageName)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取分享文本的意图
     *
     * @param content 分享文本
     * @return intent
     */
    fun getShareTextIntent(content: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, content)
        return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 文本
     * @param image   图片文件
     * @return intent
     */
    fun getShareImageIntent(content: String, image: File): Intent? {
        if (!FileUtils.isFileExists(image)) return null
        return getShareImageIntent(content, Uri.fromFile(image))
    }

    /**
     * 获取分享图片的意图
     *
     * @param content 分享文本
     * @param uri     图片uri
     * @return intent
     */
    fun getShareImageIntent(content: String, uri: Uri): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "image/*"
        return intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取其他应用组件的意图
     *
     * @param packageName 包名
     * @param className   全类名
     * @param bundle      bundle
     * @return intent
     */
    @JvmOverloads fun getComponentIntent(packageName: String, className: String, bundle: Bundle? = null): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        if (bundle != null) intent.putExtras(bundle)
        val cn = ComponentName(packageName, className)
        intent.component = cn
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取关机的意图
     * 需添加权限 `<uses-permission android:name="android.permission.SHUTDOWN"/>`
     *
     * @return intent
     */
    val shutdownIntent: Intent
        get() {
            val intent = Intent(Intent.ACTION_SHUTDOWN)
            return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

    /**
     * 获取跳至拨号界面意图
     *
     * @param phoneNumber 电话号码
     */
    fun getDialIntent(phoneNumber: String): Intent {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取拨打电话意图
     * 需添加权限 `<uses-permission android:name="android.permission.CALL_PHONE"/>`
     *
     * @param phoneNumber 电话号码
     */
    fun getCallIntent(phoneNumber: String): Intent {
        val intent = Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber))
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取跳至发送短信界面的意图
     *
     * @param phoneNumber 接收号码
     * @param content     短信内容
     */
    fun getSendSmsIntent(phoneNumber: String, content: String): Intent {
        val uri = Uri.parse("smsto:" + phoneNumber)
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.putExtra("sms_body", content)
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }


    /**
     * 获取拍照的意图
     *
     * @param outUri 输出的uri
     * @return 拍照的意图
     */
    fun getCaptureIntent(outUri: Uri): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outUri)
        return intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取选择照片的Intent
     *
     * @return
     */
    val pickIntentWithGallery: Intent
        get() {
            val intent = Intent(Intent.ACTION_PICK)
            return intent.setType("image*//*")
        }

    /**
     * 调起地图Intent
     *
     * @param place the place
     * @return the intent
     * @author : mingweigao / 2017-04-19
     */
    fun getMapIntent(place: String): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        if (AppUtils.isAppInstalled("com.baidu.BaiduMap")) {//百度地图
            intent.data = Uri.parse("baidumap://map/geocoder?src=openApiDemo&address=" + place)
        } else if (AppUtils.isAppInstalled("com.autonavi.minimap")) {//高德地图
            intent.data = Uri.parse("androidamap://poi?sourceApplication=softname&keywords=$place&dev=0")
        } else if (AppUtils.isAppInstalled("com.google.android.apps.maps")) {
            intent.data = Uri.parse("geo:0,0?q=" + place)
        } else {
            intent.data = Uri.parse("http://api.map.baidu.com/geocoder?address=" + place + "&output=html&src=" + AppUtils.applicationName)
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    /**
     * 获取从文件中选择照片的Intent
     *
     * @return
     */
    val pickIntentWithDocuments: Intent
        get() {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            return intent.setType("image*//*")
        }

    fun buildImageGetIntent(saveTo: Uri, outputX: Int, outputY: Int, returnData: Boolean): Intent {
        return buildImageGetIntent(saveTo, 1, 1, outputX, outputY, returnData)
    }

    fun buildImageGetIntent(saveTo: Uri, aspectX: Int, aspectY: Int,
                            outputX: Int, outputY: Int, returnData: Boolean): Intent {
        val intent = Intent()
        if (Build.VERSION.SDK_INT < 19) {
            intent.action = Intent.ACTION_GET_CONTENT
        } else {
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
        }
        intent.type = "image*//*"
        intent.putExtra("output", saveTo)
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", returnData)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
        return intent
    }

    fun buildImageCropIntent(uriFrom: Uri, uriTo: Uri, outputX: Int, outputY: Int, returnData: Boolean): Intent {
        return buildImageCropIntent(uriFrom, uriTo, 1, 1, outputX, outputY, returnData)
    }

    fun buildImageCropIntent(uriFrom: Uri, uriTo: Uri, aspectX: Int, aspectY: Int,
                             outputX: Int, outputY: Int, returnData: Boolean): Intent {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uriFrom, "image*//*")
        intent.putExtra("crop", "true")
        intent.putExtra("output", uriTo)
        intent.putExtra("aspectX", aspectX)
        intent.putExtra("aspectY", aspectY)
        intent.putExtra("outputX", outputX)
        intent.putExtra("outputY", outputY)
        intent.putExtra("scale", true)
        intent.putExtra("return-data", returnData)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString())
        return intent
    }

    fun buildImageCaptureIntent(uri: Uri): Intent {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        return intent
    }
}
