package com.dfm.colorpalette

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelWithHolder

class ColorRow(private val hexadecimalColor: String, private val listener: (String) -> Unit) : EpoxyModelWithHolder<ColorRow.Holder>() {
    override fun getDefaultLayout(): Int = R.layout.color_row_layout

    override fun createNewHolder(): Holder = Holder()

    override fun bind(holder: Holder) {
        holder.bind(hexadecimalColor)
    }

    inner class Holder : EpoxyHolder() {

        private lateinit var colorRow: ImageView

        override fun bindView(itemView: View) {
            colorRow = itemView.findViewById(R.id.colorRow)
        }

        fun bind(hexadecimalColor: String) {

            Log.d ("Testing", "Message " + hexadecimalColor)

            colorRow.setBackgroundColor(Color.parseColor(hexadecimalColor))

            colorRow.setOnClickListener {
                listener.invoke(hexadecimalColor)
            }
        }
    }
}
