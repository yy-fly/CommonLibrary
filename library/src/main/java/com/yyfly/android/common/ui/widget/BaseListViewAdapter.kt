package com.yyfly.android.common.ui.widget

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * ListView基础适配器
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
abstract class BaseListViewAdapter<T> : android.widget.BaseAdapter {
    /**
     * 数据集合
     */
    protected var mList: MutableList<T>? = ArrayList()
    /**
     * 关联Activity
     */
    protected var mContext: Activity

    constructor(context: Activity) {
        this.mContext = context
    }

    constructor(mContext: Activity, mList: MutableList<T>) : super() {
        this.mList = mList
        this.mContext = mContext
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    override fun getCount(): Int {
        if (mList != null)
            return mList!!.size
        else
            return 0
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    override fun getItem(position: Int): Any? {
        return if (mList == null) null else mList!![position]
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    abstract override fun getView(position: Int, convertView: View, parent: ViewGroup): View

    fun refresh(data: MutableList<T>) {
        mList = data
        notifyDataSetChanged()
    }

    fun loadMore(data: List<T>) {
        mList!!.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        if (mList != null) {
            mList!!.clear()
        }
        notifyDataSetChanged()
    }

}
