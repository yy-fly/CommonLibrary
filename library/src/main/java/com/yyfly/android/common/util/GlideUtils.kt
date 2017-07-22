package com.yyfly.android.common.util


import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.yyfly.android.common.App

/**
 * Glide工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
object GlideUtils {

    val ANDROID_RESOURCE = "android.resource://"
    val FOREWARD_SLASH = "/"

    /**
     * 将资源ID转为Uri
     *
     * @param resourceId the resource id
     * @return the uri
     */
    fun resourceIdToUri(resourceId: Int): Uri {
        return Uri.parse(ANDROID_RESOURCE + App.context.getPackageName() + FOREWARD_SLASH + resourceId)
    }

    /**
     * 加载drawable图片
     *
     * @param resId     the res id
     * @param imageView the image view
     */
    fun loadResImage(resId: Int, imageView: ImageView) {
        Glide.with(App.context)
                .load(resourceIdToUri(resId))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView)
    }


    /**
     * 加载drawable图片 不使用缓存
     *
     * @param resId     the res id
     * @param imageView the image view
     */
    fun loadResImageNoCache(resId: Int, imageView: ImageView) {
        Glide.with(App.context)
                .load(resourceIdToUri(resId))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
    }


    /**
     * 加载本地图片
     *
     * @param path      the path
     * @param imageView the image view
     */
    fun loadLocalImage(path: String, imageView: ImageView) {
        Glide.with(App.context)
                .load("file://" + path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView)
    }


    /**
     * 加载本地图片 不使用缓存
     *
     * @param path      the path
     * @param imageView the image view
     */
    fun loadLocalImageNoCache(path: String, imageView: ImageView) {
        Glide.with(App.context)
                .load("file://" + path)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
    }

    /**
     * 加载网络图片
     *
     * @param url       the url
     * @param imageView the image view
     */
    fun loadImage(url: String, imageView: ImageView) {
        Glide.with(App.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView)
    }


    /**
     * 加载网络图片 不缓存
     *
     * @param url       the url
     * @param imageView the image view
     */
    fun loadImageNoCache(url: String, imageView: ImageView) {
        Glide.with(App.context)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView)
    }

    /**
     * 加载网络Transform图片
     *
     * @param url             the url
     * @param imageView       the image view
     * @param transformations the transformations
     */
    fun loadTransformImage(url: String, imageView: ImageView, transformations: Transformation<Bitmap>) {
        Glide.with(App.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(transformations)
                .into(imageView)
    }

    /**
     * 加载drawable Transform图片.
     *
     * @param resId           the res id
     * @param imageView       the image view
     * @param transformations the transformations
     */
    fun loadTransformResImage(resId: Int, imageView: ImageView, transformations: Transformation<Bitmap>) {
        Glide.with(App.context)
                .load(resourceIdToUri(resId))
                .bitmapTransform(transformations)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView)
    }


    /**
     * 加载本地Transform图片
     *
     * @param path            the path
     * @param imageView       the image view
     * @param transformations the transformations
     */
    fun loadTransformLocalImage(path: String, imageView: ImageView, transformations: Transformation<Bitmap>) {
        Glide.with(App.context)
                .load("file://" + path)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .bitmapTransform(transformations)
                .into(imageView)
    }

}
