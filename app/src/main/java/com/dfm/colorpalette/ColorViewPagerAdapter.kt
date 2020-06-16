package com.dfm.colorpalette

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_tab.*
import java.util.*

internal class ColorViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
    private val title = arrayOf("Random", "Ordered")

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            TabFragment.getInstance(title[position])
        } else {
            TabFragment.getInstance(title[position])
        }
    }

    override fun getCount(): Int {
        return title.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}

class TabFragment : Fragment() {

    companion object {

        fun getInstance(title: String): Fragment {
            val bundle = Bundle()
            bundle.putString("title", title)
            val tabFragment = TabFragment()
            tabFragment.arguments = bundle
            return tabFragment
        }
    }

    private lateinit var adapter: ColorAdapter
    private lateinit var layoutManager: GridLayoutManager

    private val colors: ArrayList<String> = arrayListOf()
    private val orderedColors: ArrayList<String> = arrayListOf()
    private var title: String? = null

    private var originalBlue = 0
    private var originalGreen = 0
    private var originalRed = 0
    private var shouldLoad: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString("title")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        layoutManager = GridLayoutManager(context, 8)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        colorsList.layoutManager = layoutManager

        adapter = ColorAdapter()
        colorsList.adapter = this.adapter

        colorsList.addOnScrollListener(recyclerViewOnScrollListener)

        if (title == "Random") {
            randomized()
        } else {
            ordered()
        }
    }

    private fun ordered() {
        orderedColors.clear()
        loadMoreColors()
    }

    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                if (title == "Random") {
                    loadMoreRandomColors()
                } else {
                    if (shouldLoad) {
                        loadMoreColors()
                    }
                }
            }
        }
    }

    private fun makeHexadecimalFromColor(red: Int, green: Int, blue: Int): String {
        val color = Color.rgb(red, green, blue)
        val hex = Integer.toHexString(color and 0xffffff)

        val hexValue =  when(hex.length) {
            5 -> "#0$hex"
            4 -> "#00$hex"
            3 -> "#000$hex"
            2 -> "#0000$hex"
            1 -> "#00000$hex"
            else -> "#$hex"
        }

        return if (!orderedColors.contains(hexValue)) {
            orderedColors.add(hexValue)
            hexValue
        } else {
            ""
        }
    }

    private fun loadMoreColors() {

        for (i in 0..160) {

            addColorRow(makeHexadecimalFromColor(originalRed, originalGreen, originalBlue))

            if (originalBlue < 256) {
                 val newBlue = originalBlue + 1
                addColorRow(makeHexadecimalFromColor(originalRed, originalGreen, newBlue))
                originalBlue = newBlue
            } else {
                originalBlue = 0

                if (originalGreen < 256) {

                    originalBlue = if (originalBlue > 255) 0 else originalBlue

                    val newBlue = originalBlue + 1
                    val newGreen = originalGreen + 1

                    addColorRow(makeHexadecimalFromColor(originalRed, originalGreen, newBlue))
                    addColorRow(makeHexadecimalFromColor(originalRed, newGreen, originalBlue))
                    addColorRow(makeHexadecimalFromColor(originalRed, newGreen, newBlue))

                    originalBlue = newBlue
                    originalGreen = newGreen
                } else {
                    originalBlue = 0
                    originalGreen = 0

                    if (originalRed < 256) {

                        originalBlue = if (originalBlue > 255) 0 else originalBlue
                        originalGreen = if (originalGreen > 255) 0 else originalGreen

                        val newBlue = originalBlue + 1
                        val newGreen = originalGreen + 1
                        val newRed = originalRed + 1

                        addColorRow(makeHexadecimalFromColor(originalRed, originalGreen, newBlue))
                        addColorRow(makeHexadecimalFromColor(originalRed, newGreen, originalBlue))
                        addColorRow(makeHexadecimalFromColor(originalRed, newGreen, newBlue))
                        addColorRow(makeHexadecimalFromColor(newRed, originalGreen, originalBlue))
                        addColorRow(makeHexadecimalFromColor(newRed, originalGreen, newBlue))
                        addColorRow(makeHexadecimalFromColor(newRed, newGreen, originalBlue))
                        addColorRow(makeHexadecimalFromColor(newRed, newGreen, newBlue))

                        originalBlue = newBlue
                        originalGreen = newGreen
                        originalRed = newRed

                    } else {
                        originalBlue = 0
                        originalGreen = 0
                        originalRed = 0

                        shouldLoad = false
                    }
                }
            }
        }
    }

    private fun randomized() {
        colors.clear()
        loadMoreRandomColors()
    }

    private fun loadMoreRandomColors() {
        for (value in 0..250) {
            addColorRow(getRandomHexadecimalColor())
        }
    }

    private fun getRandomHexadecimalColor(): String {

        val zeros = "000000"
        val random = Random()
        var color: String

        do {
            color = Integer.toString(random.nextInt(0X1000000), 16)
            color = zeros.substring(color.length) + color

            colors.add(color)
        } while (!colors.contains(color))


        return "#$color"
    }

    private fun addColorRow(color: String) {
        Log.e("Milliner", "color " + color)
        if (!color.isNullOrEmpty()) {
            adapter.addColorRow(color) { _color ->
                Toast.makeText(context, "Color: ${_color.toUpperCase()}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
