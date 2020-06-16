package com.dfm.colorpalette

import com.airbnb.epoxy.EpoxyAdapter

class ColorAdapter: EpoxyAdapter() {

    init {
        this.enableDiffing()
    }

    fun addColorRow(hexadecimalColor: String, listener: (String) -> Unit) {
        addModel(ColorRow(hexadecimalColor, listener))
    }
}