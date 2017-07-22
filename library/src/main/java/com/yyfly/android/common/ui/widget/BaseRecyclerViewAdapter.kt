package com.yyfly.android.common.ui.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import java.util.ArrayList

/**
 * 基础适配器
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
abstract class BaseRecyclerViewAdapter<T> @JvmOverloads constructor(protected val context: Context, list: MutableList<T>? = null) : RecyclerView.Adapter<BaseRecyclerViewHolder>() {

    /**
     * Base config
     */
    private var mList: MutableList<T>? = null
    private val mInflater: LayoutInflater

    init {
        mList = list ?: ArrayList<T>()
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder {
        return onCreateNormalViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder, position: Int) {
        holder.onBindViewHolder(position)
    }

    override fun getItemCount(): Int {
        return dataCount
    }

    protected abstract val dataCount: Int

    protected abstract fun onCreateNormalViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder

    fun refresh(data: MutableList<T>) {
        mList = data
        notifyDataSetChanged()
    }

    fun loadMore(data: List<T>) {
        mList!!.addAll(data)
        notifyDataSetChanged()
    }

    fun setList(list: List<T>) {
        mList!!.clear()
        mList!!.addAll(list)
        notifyDataSetChanged()
    }

    protected fun getItem(position: Int): T {
        return mList!![position]
    }

}
