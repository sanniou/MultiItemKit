package com.sanniou.multiitemkit;

import com.sanniou.multiitem.AdapterViewHolder;

/**
 * Todo
 * Write by kotlin when kotlin 1.4 release
 */
@FunctionalInterface
public interface OnItemClickListener {
    Boolean onItemClick(AdapterViewHolder holder);
}