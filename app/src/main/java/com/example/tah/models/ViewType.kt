package com.example.tah.models

import androidx.annotation.LayoutRes

interface ViewType {

    @LayoutRes
    fun getBasicView(): Int

    @LayoutRes
    fun getAddView(): Int

    @LayoutRes
    fun getItemView(): Int
}