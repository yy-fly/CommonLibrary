package com.yyfly.android.common.ui.widget

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * RecyclerView Holder
 *
 * @author : yyfly / developer@yyfly.com
 * @version : 1.0
 */
abstract class BaseRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener { v -> onItemClick(v, adapterPosition) }
    }

    abstract fun onBindViewHolder(position: Int)
    abstract fun onItemClick(view: View, position: Int)
}
