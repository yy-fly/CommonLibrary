package com.yyfly.android.common.util

import android.os.Handler
import android.os.Message

import java.lang.ref.WeakReference

/**
 * Handler相关工具类
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
class HandlerUtils {

    class HandlerHolder
    /**
     * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
     *
     * @param listener 收到消息回调接口
     */
    (listener: OnReceiveMessageListener) : Handler() {
        internal var mListenerWeakReference: WeakReference<OnReceiveMessageListener>? = null

        init {
            mListenerWeakReference = WeakReference(listener)
        }

        override fun handleMessage(msg: Message) {
            if (mListenerWeakReference != null && mListenerWeakReference!!.get() != null) {
                mListenerWeakReference!!.get()?.handlerMessage(msg)
            }
        }
    }

    /**
     * 收到消息回调接口
     */
    interface OnReceiveMessageListener {
        fun handlerMessage(msg: Message)
    }
}
